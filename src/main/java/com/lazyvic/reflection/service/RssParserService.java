package com.lazyvic.reflection.service;

import com.lazyvic.reflection.model.NewsItem;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class RssParserService {
    private static final Logger log = LoggerFactory.getLogger(RssParserService.class);
    private final RedisService redisService;

    public RssParserService(RedisService redisService) {
        this.redisService = redisService;
    }

    public List<NewsItem> fetchNewsWithCache(String feedUrl) {
        try {
            String cacheKey = "rss:raw:" + feedUrl;
            String rawXml = redisService.getCachedRssString(cacheKey);
            SyndFeed feed;

            if (rawXml != null) {
                log.info("[hit cache] " + feedUrl);
                feed = new SyndFeedInput().build(new XmlReader(new ByteArrayInputStream(rawXml.getBytes(StandardCharsets.UTF_8))));
            } else {
                URL url = new URL(feedUrl);
                InputStream is = url.openStream();
                byte[] raw = is.readAllBytes();
                rawXml = new String(raw, StandardCharsets.UTF_8);

                redisService.cacheRssString(cacheKey, rawXml, 300);
                feed = new SyndFeedInput().build(new XmlReader(new ByteArrayInputStream(raw)));
            }

            return feed.getEntries().stream().map(NewsItem::form).toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public List<NewsItem> fetchNews(String feedUrl) {
        try {
            String cacheKey = "rss:raw" + feedUrl;
            URL url = new URL(feedUrl);
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));


            return feed.getEntries().stream()
                    .map(NewsItem::form)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
