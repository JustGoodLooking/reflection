package com.lazyvic.reflection.dto;

import java.io.Serial;
import java.io.Serializable;

public class PlanDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long telegramId;
    private String name;
    private String title;
    private String description;

    public PlanDto(Long telegramId, String title, String description, String name) {
        this.telegramId = telegramId;
        this.title = title;
        this.description = description;
        this.name = name;
    }

    public Long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
