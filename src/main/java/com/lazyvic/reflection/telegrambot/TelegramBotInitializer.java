package com.lazyvic.reflection.telegrambot;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Component
public class TelegramBotInitializer {

    private final TelegramLongPollingBot telegramBot;

    public TelegramBotInitializer(TelegramLongPollingBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {

        List<BotCommand> commands = List.of(
                new BotCommand("/news", "看最新新聞")
        );
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            telegramBot.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
            telegramBot.execute(new DeleteWebhook());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
