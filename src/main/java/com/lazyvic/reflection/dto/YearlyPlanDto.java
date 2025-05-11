package com.lazyvic.reflection.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class YearlyPlanDto extends PlanDto{

    private String date;
    public YearlyPlanDto(UpdateMessageDto updateMessageDto) {
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

    public static YearlyPlanDto convert(UpdateMessageDto updateMessageDto) {
        return new YearlyPlanDto(updateMessageDto);
    }

    private String getCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }
}
