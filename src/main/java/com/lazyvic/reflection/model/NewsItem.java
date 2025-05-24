package com.lazyvic.reflection.model;

import com.lazyvic.reflection.events.model.RssPushEvent;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.Date;

public class NewsItem {
    public final String title;
    public final String link;
    public final Date publishedDate;

    public NewsItem(String title, String link, Date publishedDate) {
        this.title = title;
        this.link = link;
        this.publishedDate = publishedDate;
    }

    public static NewsItem form(SyndEntry entry) {
        return new NewsItem(
                entry.getTitle(),
                entry.getLink(),
                entry.getPublishedDate()
        );
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public RssPushEvent toPushEvent(Long chatId, NewsItem item) {
        return new RssPushEvent(chatId, item.getTitle(), item.getLink());
    }
}