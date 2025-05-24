package com.lazyvic.reflection.service;

import com.lazyvic.reflection.model.NewsItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RssParserServiceTest {
    private final RssParserService parserService = new RssParserService(redisService);

    @Test
    void shouldParseValidFeed() {
        String feedUrl = "https://feeds.feedburner.com/ettoday/global";

        List<NewsItem> newsItems = parserService.fetchNews(feedUrl);

        assertNotNull(newsItems);
        assertFalse(newsItems.isEmpty());
        assertNotNull(newsItems.get(0).title);
        assertNotNull(newsItems.get(0).link);
    }
}
