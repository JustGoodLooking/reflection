package com.lazyvic.reflection.telegrambot;

import com.lazyvic.reflection.dto.BotNewsResponse;
import com.lazyvic.reflection.dto.DailyPlanDto;
import com.lazyvic.reflection.dto.UpdateCallbackQueryDto;
import com.lazyvic.reflection.dto.UpdateMessageDto;
import com.lazyvic.reflection.events.model.RssPushEvent;
import com.lazyvic.reflection.events.model.UserActionEvent;
import com.lazyvic.reflection.events.producer.RssPushEventProducer;
import com.lazyvic.reflection.events.producer.UserActionEventProducer;
import com.lazyvic.reflection.model.DailyPlan;
import com.lazyvic.reflection.repository.DailyPlanRepository;
import com.lazyvic.reflection.service.ChecklistService;
import com.lazyvic.reflection.service.DailyPlanService;
import com.lazyvic.reflection.service.NewsRecommendationService;
import com.lazyvic.reflection.service.RssParserService;
import com.lazyvic.reflection.util.ParsedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class RefectionBot extends TelegramLongPollingBot {
    private static final String DAILY = "Daily";

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    private DailyPlanService dailyPlanService;
    private final DailyPlanRepository dailyPlanRepository;
    private UserActionEventProducer userActionEventProducer;
    private final RssParserService rssParserService;
    private final NewsRecommendationService newsRecommendationService;
    private final RssPushEventProducer rssPushEventProducer;
    private final ChecklistService checklistService;

    public RefectionBot(DailyPlanService dailyPlanService,
                        DailyPlanRepository dailyPlanRepository,
                        UserActionEventProducer userActionEventProducer,
                        RssParserService rssParserService,
                        NewsRecommendationService newsRecommendationService,
                        RssPushEventProducer rssPushEventProducer,
                        ChecklistService checklistService) {
        this.dailyPlanService = dailyPlanService;
        this.dailyPlanRepository = dailyPlanRepository;
        this.userActionEventProducer = userActionEventProducer;
        this.rssParserService = rssParserService;
        this.newsRecommendationService = newsRecommendationService;
        this.rssPushEventProducer = rssPushEventProducer;
        this.checklistService = checklistService;
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
        // handle message
        if (update.hasMessage()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        } else {
            System.out.println("unsupported update type");
        }
    }

    private void handleMessage(Update update) {
        UpdateMessageDto updateMessageDto = UpdateMessageDto.convert(update);
        ParsedMessage parsed = updateMessageDto.parseMessage();

        String command = parsed.getTitle();
        List<String> params = parsed.getParameters();


        switch (command) {
            case "news" -> handleNewsFeed(updateMessageDto);
            case "checklist" -> handleCheckList(updateMessageDto);
            case "shortURL" -> handleShortUrl(updateMessageDto);
            default -> handleUnknown(updateMessageDto);
        }

//        if (params.stream().anyMatch(param -> param.equalsIgnoreCase("year"))) {
//            handleYearLogic(updateMessageDto);
//            return;
//        }
//
//
//        boolean handleDailyLogicResult = handleDailyLogic(updateMessageDto);
//        if (handleDailyLogicResult) {
//            publishUserActionEvent(updateMessageDto);
//        }
//        replyAddPlan(update, handleDailyLogicResult);



    }

    private void handleUnknown(UpdateMessageDto updateMessageDto) {

    }

    private void handleShortUrl(UpdateMessageDto updateMessageDto) {
        // valid parameter
        List<String> params = updateMessageDto.parseMessage().getParameters();
    }

    private void handleCheckList(UpdateMessageDto updateMessageDto) {
        List<ChecklistService.Item> items = checklistService.getChecklist(updateMessageDto.getChatId());
        String text = checklistService.renderText(items);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (ChecklistService.Item item : items) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText((item.done() ? "✅" : "⬜") + " " + item.text());
            btn.setCallbackData("toggle:" + item.id());
            buttons.add(List.of(btn));
        }
        markup.setKeyboard(buttons);
        sendMessageToUser(updateMessageDto.getChatId(), text, markup);
    }

    private void publishUserActionEvent(UpdateMessageDto updateMessageDto) {
        UserActionEvent event = new UserActionEvent(
                updateMessageDto.getUserId(),
                DAILY,
                updateMessageDto.getTextMessage()
        );
        userActionEventProducer.send(event);
    }

    private void handleCallbackQuery(Update update) {
        UpdateCallbackQueryDto updateCallbackQueryDto = UpdateCallbackQueryDto.convert(update);
        if (updateCallbackQueryDto.getCallbackData().startsWith("toggle:")) {
            checkList(updateCallbackQueryDto);
            return;
        }

        Long userTelegramId = updateCallbackQueryDto.getUserId();
        List<DailyPlan> dailyPlans = dailyPlanService.getUserDailyPlansByJPQL(userTelegramId);

        String formatDailyPlansForTelegram = formatDailyPlansForTelegram(dailyPlans);


        replyDailyPlan(update, formatDailyPlansForTelegram);
    }

    private void checkList(UpdateCallbackQueryDto updateCallbackQueryDto) {
        String data = updateCallbackQueryDto.getCallbackData();
        Long userId = updateCallbackQueryDto.getUserId();
        Integer messageId = updateCallbackQueryDto.getMessageId();
        Long chatId = updateCallbackQueryDto.getChatId();
        int id = Integer.parseInt(data.split(":" )[1]);
        checklistService.toggleItem(userId, id);
        List<ChecklistService.Item> items = checklistService.getChecklist(userId);

        boolean allDone = items.stream().allMatch(ChecklistService.Item::done);
        if (allDone) {
            checklistService.resetChecklist(userId);
            editMessageText(chatId, messageId, "✅ Checklist 完成！已自動重置。", null);
        } else {
            String newText = checklistService.renderText(items);
            InlineKeyboardMarkup markup = buildNewMarkup(items);
            editMessageText(chatId, messageId, newText, markup);
        }
    }

    public void editMessageText(Long chatId, Integer messageId, String newText, InlineKeyboardMarkup markup)  {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(newText);
        editMessage.setReplyMarkup(markup);
        editMessage.setParseMode("HTML");

        try {
            execute(editMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public InlineKeyboardMarkup buildNewMarkup(List<ChecklistService.Item> items) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (ChecklistService.Item item : items) {
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText((item.done() ? "✅" : "⬜") + " " + item.text());
            btn.setCallbackData("toggle:" + item.id());
            buttons.add(List.of(btn));
        }
        markup.setKeyboard(buttons);
        return markup;
    }


    private void handleYearLogic(UpdateMessageDto updateMessageDto) {
        System.out.println("save year plan");
    }

    private boolean handleDailyLogic(UpdateMessageDto updateMessageDto) {
        DailyPlanDto dailyPlanDto = DailyPlanDto.convert(updateMessageDto);
        return dailyPlanService.trySaveUserPlan(dailyPlanDto);
    }

    private void handleNewsFeed(UpdateMessageDto updateMessageDto) {
        BotNewsResponse response = newsRecommendationService.fetchNews(updateMessageDto);


        if (response.isCooldown()) {
            sendMessageToUser(updateMessageDto.getChatId(), response.getMessage());
            return;
        }

        if (response.newsItemList.isEmpty()) {
            sendMessageToUser(updateMessageDto.getChatId(), response.getMessage());
            return;
        }

        List<RssPushEvent> events = response.getNewsItemList().stream()
                .map(item -> item.toPushEvent(updateMessageDto.getChatId(), item))
                .toList();


        rssPushEventProducer.send(events);


//        newsItemList.stream()
//                .map(NewsItem::getLink)
//                .limit(3)
//                .forEach(msg -> sendMessageToUser(updateMessageDto.getChatId(), msg));

    }

    private void replyDailyPlan(Update update, String formatDailyPlansForTelegram) {

        // 创建按钮
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        // 创建一个按钮
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Menu ");
        button.setCallbackData("button_click"); // 设置按钮的回调数据

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        buttons.add(row);
        keyboardMarkup.setKeyboard(buttons);

        // 发送消息和按钮
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setText(formatDailyPlansForTelegram);
        sendMessage.setReplyMarkup(keyboardMarkup); // 添加按钮到消息中

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replyAddPlan(Update update, boolean handlePlanResult) {
        String buttonText = getButtonText(handlePlanResult);


        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();


        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Check Today's Plans ");
        button.setCallbackData("button_click"); // 设置按钮的回调数据

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        buttons.add(row);
        keyboardMarkup.setKeyboard(buttons);

        // 发送消息和按钮
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(buttonText);
        sendMessage.setReplyMarkup(keyboardMarkup); // 添加按钮到消息中

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String formatDailyPlansForTelegram(List<DailyPlan> dailyPlans) {
        StringBuilder message = new StringBuilder();

        for (DailyPlan dailyPlan : dailyPlans) {
            message.append("title: ").append(dailyPlan.getTitle()).append("\n");
            message.append("description: ").append(dailyPlan.getDescription()).append("\n");
            message.append("--------\n");
        }

        return message.toString();
    }

    public void sendMessageToUser(Long chatId, String message) {
        sendMessageToUser(chatId, message, null);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(chatId.toString());
//        sendMessage.setText(message);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }

    public void sendMessageToUser(Long chatId, String message, InlineKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        if (markup != null) {
            sendMessage.setReplyMarkup(markup);
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getButtonText(boolean savePlanResult) {
        if (!savePlanResult) {
            return "too many time, try to take a rest...";
        }
        return "success add plan..";
    }

    private void userActionEvent() {
        
    }

    private void eventTest() {
        // test event
        int x = 0;
        while (x < 10) {
            String message = "test rabbit #" + x;
            UserActionEvent uae = new UserActionEvent(123L, "/add", message);
            userActionEventProducer.send(uae);
            x++;
        }
    }


}
