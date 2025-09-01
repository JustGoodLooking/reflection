package com.lazyvic.reflection.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChecklistService {
    private final RedisService redisService;

    public ChecklistService(RedisService redisService) {
        this.redisService = redisService;
    }


    public record Item(int id, String text, boolean done) {
        public String render() {
            return (done ? "\u2611" : "\u2610") + " " + text;
        }
    }

    private String redisKey(Long userId) {
        return "checklist:" + LocalDate.now() + ":" + userId;
    }

    public List<Item> getChecklist(Long userId) {
        String key = redisKey(userId);
        List<String> raw = redisService.getList(key);
        if (raw == null || raw.isEmpty()) {
            List<Item> defaultItems = getDefaultItems();
            saveChecklist(userId, defaultItems);
            return defaultItems;
        }
        return raw.stream().map(this::deserialize).toList();
    }

    public void toggleItem(Long userId, int itemId) {
        List<Item> list = getChecklist(userId);
        List<Item> updated = new ArrayList<>();
        for (Item item : list) {
            if (item.id == itemId) {
                updated.add(new Item(item.id, item.text, !item.done));
            } else {
                updated.add(item);
            }
        }
        saveChecklist(userId, updated);
    }

    public void saveChecklist(Long userId, List<Item> items) {
        String key = redisKey(userId);
        List<String> serialized = items.stream().map(this::serialize).toList();
        redisService.setList(key, serialized);
        redisService.setExpireAtMidnight(key);
    }

    public String renderText(List<Item> items) {
//        StringBuilder sb = new StringBuilder("\uD83E\uDDF3 出門前 Checklist:\n\n");
//        for (Item item : items) {
//            sb.append(item.render()).append("\n");
//        }
//        return sb.toString();
        return "🧾 請點選完成項目：";
    }

    private String serialize(Item item) {
        return item.id + "," + item.text + "," + item.done;
    }

    private Item deserialize(String s) {
        String[] parts = s.split(",", 3);
        return new Item(Integer.parseInt(parts[0]), parts[1], Boolean.parseBoolean(parts[2]));
    }

    public void resetChecklist(Long userId) {
        List<Item> defaultItems = getDefaultItems();
        saveChecklist(userId, defaultItems);
    }

    private List<Item> getDefaultItems() {
        return List.of(
                new Item(1, "[房間] 電", false),
                new Item(2, "[廚房] 電", false),
                new Item(3, "[廚房] 火", false),
                new Item(4, "[廚房] 水", false),
                new Item(5, "[客廳] 電" , false)
        );
    }
}
