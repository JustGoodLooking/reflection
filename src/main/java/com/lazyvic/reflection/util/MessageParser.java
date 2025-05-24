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
            return parseMessage();
        }
    }

    private ParsedMessage parseMessage() {
        String[] lines = textMessage.split("\n");
        String title = lines.length > 0 ? lines[0] : "";
        String description = lines.length > 1 ? lines[1] : "";
        List<String> parameters = lines.length > 2 ? Arrays.asList(lines[2].trim().split(" ")) : new ArrayList<>();

        return new ParsedMessage(title, description, parameters);
    }

    private ParsedMessage parseCommand() {
        String command = textMessage.split(" ")[0];
        String params = textMessage.length() > command.length() ? textMessage.substring(command.length()).trim() : "";

        // 处理未知命令
        if (command.equals("/help")) {
            return new ParsedMessage("Help Command", "This is help information.", List.of(params));
        } else {
            // 对于其他未知命令，可以返回特定的处理或消息
            return new ParsedMessage("news", "Command not recognized: " + command, List.of(params));
        }
    }
}