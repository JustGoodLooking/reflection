package com.lazyvic.reflection.dto;

import org.telegram.telegrambots.meta.api.objects.Update;

// UpdateCallbackQueryDto.java
public class UpdateCallbackQueryDto {
    private Long userId;
    private String callbackData;
    private Long chatId;
    private Integer messageId;
    private String chatInstance;

    public UpdateCallbackQueryDto(Update update) {
        this.userId = update.getCallbackQuery().getFrom().getId();
        this.callbackData = update.getCallbackQuery().getData();
        this.chatId = update.getCallbackQuery().getMessage().getChatId();
        this.messageId = update.getCallbackQuery().getMessage().getMessageId();
        this.chatInstance = update.getCallbackQuery().getChatInstance();
    }

    public static UpdateCallbackQueryDto convert(Update update) {
        return new UpdateCallbackQueryDto(update);
    }

    public Long getUserId() {
        return userId;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public Long getChatId() {
        return chatId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public String getChatInstance() {
        return chatInstance;
    }

    // Getter 和 Setter 方法
}