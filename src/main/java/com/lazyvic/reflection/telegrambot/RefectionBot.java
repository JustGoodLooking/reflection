package com.lazyvic.reflection.telegrambot;

import com.lazyvic.reflection.dto.DailyPlanDto;
import com.lazyvic.reflection.dto.UpdateMessageDto;
import com.lazyvic.reflection.service.DailyPlanService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class RefectionBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    private DailyPlanService dailyPlanService;

    public RefectionBot(DailyPlanService dailyPlanService) {
        this.dailyPlanService = dailyPlanService;
    }


    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("update +" +update);
        if (update.hasMessage()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            System.out.println("callbackquery");
//            handleCallbackQuery(update.getCallbackQuery());
        } else {
            System.out.println("unsupported update type");
        }
    }

    private void handleMessage(Update update) {
        UpdateMessageDto updateMessageDto = UpdateMessageDto.convert(update);
        List<String> params = updateMessageDto.parseMessage().getParameters();

        if (params.stream().anyMatch(param -> param.equalsIgnoreCase("year"))) {
            handleYearLogic(updateMessageDto);
        } else {
            saveUserPlan(updateMessageDto);
        }

        replyTest(update);
    }

//    private void handleCallbackQuery(CallbackQuery callbackQuery) {
//        System.out.println("handle callback");
//    }

    private void handleYearLogic(UpdateMessageDto updateMessageDto) {
        // 执行 year 相关的处理逻辑
    }

    private void saveUserPlan(UpdateMessageDto updateMessageDto) {
        DailyPlanDto dailyPlanDto = DailyPlanDto.convert(updateMessageDto);
        dailyPlanService.saveUserPlan(dailyPlanDto);
    }

    private void replyTest(Update update) {
        String message = update.getMessage().getText();
        System.out.println("Received message: " + message);

        // 创建按钮
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        // 创建一个按钮
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Click Me! ");
        button.setCallbackData("button_click"); // 设置按钮的回调数据

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        buttons.add(row);
        keyboardMarkup.setKeyboard(buttons);

        // 发送消息和按钮
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("Are you saying:"  + message + "? ");
        sendMessage.setReplyMarkup(keyboardMarkup); // 添加按钮到消息中

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
