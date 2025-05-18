package com.lazyvic.reflection.telegrambot;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBotInitializer {

    private final TelegramLongPollingBot telegramBot;

    public TelegramBotInitializer(TelegramLongPollingBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            telegramBot.execute(new DeleteWebhook());

            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
