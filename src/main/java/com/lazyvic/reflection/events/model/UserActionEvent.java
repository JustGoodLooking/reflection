package com.lazyvic.reflection.events.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserActionEvent implements Serializable {

    private Long userId;
    private String action;
    private String messageText;
    private String traceId;
    private LocalDateTime timestamp;

    public UserActionEvent() {
        // for deserialization
    }

    public UserActionEvent(Long userId, String action, String messageText) {
        this.userId = userId;
        this.action = action;
        this.messageText = messageText;
        this.timestamp = LocalDateTime.now();
        this.traceId = UUID.randomUUID().toString();
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "[traceId=" + traceId + "] userId=" + userId +
                ", action='" + action + '\'' +
                ", messageText='" + messageText + '\'' +
                ", timestamp=" + timestamp;
    }
}