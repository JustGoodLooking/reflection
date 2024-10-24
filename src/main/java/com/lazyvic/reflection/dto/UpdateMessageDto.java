package com.lazyvic.reflection.dto;

import com.lazyvic.reflection.util.MessageParser;
import com.lazyvic.reflection.util.ParsedMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UpdateMessageDto {
    private Long userId;
    private String userName;
    private String textMessage;
    private Long chatId;
    private String chatType;
    private LocalDateTime messageTime;

    public UpdateMessageDto(Update update) {
        this.userId = update.getMessage().getFrom().getId();
        this.userName = update.getMessage().getFrom().getFirstName();
        this.textMessage = update.getMessage().getText();
        this.chatId = update.getMessage().getChat().getId();
        this.chatType = update.getMessage().getChat().getType();
        this.messageTime = LocalDateTime.ofEpochSecond(update.getMessage().getDate(), 0, ZoneOffset.UTC);
    }

    public static UpdateMessageDto convert(Update update) {
        return new UpdateMessageDto(update);
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getChatType() {
        return chatType;
    }

    public LocalDateTime getMessageTime() {
        return messageTime;
    }

    public ParsedMessage parseMessage() {
        MessageParser parser = new MessageParser(textMessage);
        return parser.parse();
    }

}