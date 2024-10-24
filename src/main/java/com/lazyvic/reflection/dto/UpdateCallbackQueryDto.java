package com.lazyvic.reflection.dto;

import org.telegram.telegrambots.meta.api.objects.Update;

// UpdateCallbackQueryDto.java
public class UpdateCallbackQueryDto {
    private Long userId;
    private String callbackData;
    private Long chatId;
    private Integer messageId;
    private String chatInstance;

    public CallbackQueryDto(Update update) {
        this.userId = update.getCallbackQuery().getFrom().getId();
        this.callbackData = update.getCallbackQuery().getData();
        this.chatId = update.getCallbackQuery().getMessage().getChatId();
        this.messageId = update.getCallbackQuery().getMessage().getMessageId();
        this.chatInstance = update.getCallbackQuery().getChatInstance();
    }
    // Getter 和 Setter 方法
}