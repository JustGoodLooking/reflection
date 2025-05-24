package com.lazyvic.reflection.scheduler;

import com.lazyvic.reflection.model.NewsItem;
import com.lazyvic.reflection.service.RedisService;
import com.lazyvic.reflection.service.RssParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RssCacheWarmupScheduler {
    private static final Logger logger = LoggerFactory.getLogger(RssCacheWarmupScheduler.class);
    private final RssParserService rssParserService;
    private final Map<String, String> rssUrlMap;

    public RssCacheWarmupScheduler(RssParserService rssParserService, Map<String, String> rssUrlMap) {
        this.rssParserService = rssParserService;
        this.rssUrlMap = rssUrlMap;
    }

    @Scheduled(fixedRate = 180_000)
    public void warmRssCache() {
        rssUrlMap.forEach((category, url) -> {
            try {
                List<NewsItem> items = rssParserService.fetchNewsWithCache(url);
                logger.info("[warm RSS cache] " + category + " - " + items.size() + " items");

            } catch (Exception e) {
                logger.info("[warm RSS cache failed] " + category + ": " + e.getMessage());
            }
        });
    }

}
