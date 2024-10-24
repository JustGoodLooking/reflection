package com.lazyvic.reflection.dto;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyPlanDto extends PlanDto{

    private String date;
    public DailyPlanDto(UpdateMessageDto updateMessageDto) {
        super(
                updateMessageDto.getUserId(),
                updateMessageDto.parseMessage().getTitle(),
                updateMessageDto.parseMessage().getDescription(),
                updateMessageDto.getUserName()
                );
        this.date = getCurrentDate();

    }

    public String getDate() {
        return date;
    }

    public static DailyPlanDto convert(UpdateMessageDto updateMessageDto) {
        return new DailyPlanDto(updateMessageDto);
    }

    private String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }
}
