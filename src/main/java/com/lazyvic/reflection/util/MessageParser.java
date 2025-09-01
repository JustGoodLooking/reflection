package com.lazyvic.reflection.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageParser {
    private final String textMessage;

    public MessageParser(String textMessage) {
        this.textMessage = textMessage;
    }


    public ParsedMessage parse() {
        if (textMessage.startsWith("/")) {
            return parseCommand();
        } else {
            return parsePlainMessage();
        }
    }

    private ParsedMessage parsePlainMessage() {
        String[] lines = textMessage.split("\n");
        String title = lines.length > 0 ? lines[0] : "";
        String description = lines.length > 1 ? lines[1] : "";
        List<String> parameters = lines.length > 2
                ? Arrays.asList(lines[2].trim().split(" "))
                : List.of();

        return new ParsedMessage(title, description, parameters);
    }

    private ParsedMessage parseCommand() {
        String[] parts = textMessage.trim().split(" ", 2);
        String command = parts[0].substring(1); // 去掉前面的 "/"
        List<String> parameters = parts.length > 1
                ? Arrays.asList(parts[1].trim().split(" "))
                : List.of();

        return switch (command) {
            case "checklist" -> new ParsedMessage("checklist", "Checklist command", parameters);
            case "news" -> new ParsedMessage("news", "News command", parameters);
            case "shorturl" -> new ParsedMessage("shorturl", "Shorten URL command", parameters);
            default -> new ParsedMessage("help", "Unsupported command: " + command, parameters);
        };
    }
}