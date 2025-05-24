package com.lazyvic.reflection.service;

import com.lazyvic.reflection.dto.BotNewsResponse;
import com.lazyvic.reflection.dto.UpdateMessageDto;
import com.lazyvic.reflection.model.CategoryQuota;
import com.lazyvic.reflection.model.NewsItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class NewsRecommendationService {
    private final RedisService redisService;
    private final RssParserService rssParserService;
    private final List<CategoryQuota> categoryQuotas;
    private final Map<String, String> rssUrlMap;
    private static final int TOTAL_TO_PUSH = 5;

    public NewsRecommendationService(RedisService redisService, RssParserService rssParserService, List<CategoryQuota> categoryQuotas, Map<String, String> rssUrlMap) {
        this.redisService = redisService;
        this.rssParserService = rssParserService;
        this.categoryQuotas = categoryQuotas;
        this.rssUrlMap = rssUrlMap;
    }

    public BotNewsResponse fetchNews(UpdateMessageDto updateMessageDto) {
        if (redisService.isAskCooldownActive(updateMessageDto.getUserId())) {
            long time = redisService.getCooldownRemainingSeconds(updateMessageDto.getUserId());
            String message = "Whoa! Easy tiger. Cooling down for " + time + " seconds...";
            return new BotNewsResponse(BotNewsResponse.Status.COOLDOWN, message);
        }
        String url = "https://feeds.feedburner.com/ettoday/teck3c";
//        List<NewsItem> newsItems = rssParserService.fetchNews(url);

        List<NewsItem> newsItems = fetchNews(updateMessageDto.getUserId());
        String message = null;
        if (newsItems.isEmpty()) {
            message = "Nothing news... Yet.";
        }
        redisService.activateAskCoolDown(updateMessageDto.getUserId());
        return new BotNewsResponse(BotNewsResponse.Status.DELIVERABLE, newsItems, message);

    }

    private List<NewsItem> fetchNews(Long userId) {
        long start = System.currentTimeMillis();
        List<CategoryQuota> activeQuotas = new ArrayList<>(categoryQuotas);
        List<NewsItem> waitingToPush = new ArrayList<>();

        while (!activeQuotas.isEmpty() && waitingToPush.size() < TOTAL_TO_PUSH) {
            int remaining = TOTAL_TO_PUSH - waitingToPush.size();
            redistributeQuota(activeQuotas, remaining);

            Iterator<CategoryQuota> iterator = activeQuotas.iterator();
            while (iterator.hasNext()) {

                if (waitingToPush.size() >= TOTAL_TO_PUSH) {
                    break;
                }

                CategoryQuota q = iterator.next();

                long loopStart = System.currentTimeMillis();
                System.out.println("[分類] 處理 " + q.category);

                String link = rssUrlMap.get(q.category);
                List<NewsItem> fresh = rssParserService.fetchNewsWithCache(link)
                        .stream()
                        .filter(newsItem -> !redisService.isNewsAlreadySent(userId, newsItem.link))
                        .filter(newsItem -> waitingToPush.stream().noneMatch(i -> i.link.equals(newsItem.link)))
                        .limit(q.quota)
                        .toList();


                waitingToPush.addAll(fresh);
                System.out.println("[分類] " + q.category + " 處理耗時: " + (System.currentTimeMillis() - loopStart) + " ms");
                if (fresh.isEmpty()) {
                    iterator.remove();
                }
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("[推播] pushNewsWithDynamicFallback 耗時: " + elapsed + " ms");
        return waitingToPush;

    }


    private void redistributeQuota(List<CategoryQuota> activeQuotas, int totalToPush) {
        int totalWeight = activeQuotas.stream()
                .mapToInt(q -> q.weight)
                .sum();

        int totalAssigned = 0;
        for (int i = 0; i < activeQuotas.size(); i++) {
            CategoryQuota q = activeQuotas.get(i);
            q.quota = (int) Math.ceil((double) q.weight / totalWeight * totalToPush);
            totalAssigned += q.quota;
//            if (i < activeQuotas.size() - 1) {
//                q.quota = (int) Math.ceil((double) q.weight / totalWeight * totalToPush);
//                totalAssigned += q.quota;
//            } else {
//                q.quota = totalToPush - totalAssigned;
//            }
        }



    }
}
