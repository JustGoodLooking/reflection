package com.lazyvic.reflection.dto;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyPlanDto extends PlanDto{

    private String date;
    public DailyPlanDto(Update update) {
        super(
                update.getMessage().getFrom().getId(),
                update.getMessage().getText(),
                "description",
                update.getMessage().getFrom().getFirstName()
                );
        this.date = getCurrentDate();

    }

    public String getDate() {
        return date;
    }

    public static DailyPlanDto convert(Update update) {
        return new DailyPlanDto(update);
    }

    private String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }
}
