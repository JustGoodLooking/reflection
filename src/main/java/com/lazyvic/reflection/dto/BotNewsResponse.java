package com.lazyvic.reflection.dto;

import com.lazyvic.reflection.model.NewsItem;

import java.util.List;

public class BotNewsResponse {
    public enum Status{
        COOLDOWN, DELIVERABLE
    }

    public Status status;
    public List<NewsItem> newsItemList;
    public String message;

    public BotNewsResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public BotNewsResponse(Status status, List<NewsItem> newsItemList, String message) {
        this.status = status;
        this.newsItemList = newsItemList;
        this.message = message;
    }

    public boolean isCooldown() {
        return status == Status.COOLDOWN;
    }

    public String getMessage() {
        return message;
    }

    public List<NewsItem> getNewsItemList() {
        return newsItemList;
    }
}
