package com.lazyvic.reflection.util;


import java.util.List;

public class ParsedMessage {
    private final String title;
    private final String description;
    private final List<String> parameters;

    public ParsedMessage(String title, String description, List<String> parameters) {
        this.title = title;
        this.description = description;
        this.parameters = parameters;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getParameters() {
        return parameters;
    }
}