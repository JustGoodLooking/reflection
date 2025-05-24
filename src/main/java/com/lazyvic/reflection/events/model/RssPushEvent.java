package com.lazyvic.reflection.events.model;

import java.io.Serializable;
import java.util.UUID;

public class RssPushEvent implements Serializable {

    private Long userId;
    private String messageText = "push";
    private String title;
    private String link;
    private String traceId;


    public RssPushEvent() {
        // for deserialization
    }

    public RssPushEvent(Long userId, String action, String messageText) {
        this.userId = userId;
        this.title = action;
        this.link = messageText;
        this.traceId = UUID.randomUUID().toString();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getMessageText() {
        return messageText;
    }
}