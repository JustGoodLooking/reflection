package com.lazyvic.reflection.config;

import com.lazyvic.reflection.model.CategoryQuota;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class RssConfig {

    @Bean
    public List<CategoryQuota> categoryQuotas() {
        return List.of(
                new CategoryQuota("realtime", 100),
                new CategoryQuota("finance", 99),
                new CategoryQuota("tech", 98),
                new CategoryQuota("world", 97),
                new CategoryQuota("law", 96),
                new CategoryQuota("insurance", 95),
                new CategoryQuota("house", 94),
                new CategoryQuota("commentary", 93),
                new CategoryQuota("global", 92),
                new CategoryQuota("army", 91),
                new CategoryQuota("china", 90),
                new CategoryQuota("novelty", 89),
                new CategoryQuota("netsearch", 88),
                new CategoryQuota("dalemon", 87),
                new CategoryQuota("lifestyle", 86),
                new CategoryQuota("family", 85),
                new CategoryQuota("health", 84),
                new CategoryQuota("society", 83),
                new CategoryQuota("charity", 82),
                new CategoryQuota("local", 81),
                new CategoryQuota("pet", 80),
                new CategoryQuota("star", 79),
                new CategoryQuota("movies", 78),
                new CategoryQuota("gender", 77),
                new CategoryQuota("sport", 76),
                new CategoryQuota("travel", 75),
                new CategoryQuota("consuming", 74),
                new CategoryQuota("success", 73),
                new CategoryQuota("fashion", 72),
                new CategoryQuota("speed", 71),
                new CategoryQuota("teck3c", 70),
                new CategoryQuota("game", 69),
                new CategoryQuota("citizen", 68),
                new CategoryQuota("master", 67),
                new CategoryQuota("fortune", 66),
                new CategoryQuota("photo", 65)
        );
    }

    @Bean
    public Map<String, String> rssUrlMap() {
        return Map.ofEntries(
                Map.entry("realtime", "https://feeds.feedburner.com/ettoday/realtime"),
                Map.entry("finance", "https://feeds.feedburner.com/ettoday/finance"),
                Map.entry("tech", "https://feeds.feedburner.com/ettoday/teck3c"),
                Map.entry("world", "https://feeds.feedburner.com/ettoday/global"),
                Map.entry("law", "https://feeds.feedburner.com/ettoday/law"),
                Map.entry("insurance", "https://feeds.feedburner.com/ettoday/insurance"),
                Map.entry("house", "https://feeds.feedburner.com/ettoday/house"),
                Map.entry("commentary", "https://feeds.feedburner.com/ettoday/commentary"),
                Map.entry("global", "https://feeds.feedburner.com/ettoday/global"),
                Map.entry("army", "https://feeds.feedburner.com/ettoday/army"),
                Map.entry("china", "https://feeds.feedburner.com/ettoday/china"),
                Map.entry("novelty", "https://feeds.feedburner.com/ettoday/novelty"),
                Map.entry("netsearch", "https://feeds.feedburner.com/ettoday/netsearch"),
                Map.entry("dalemon", "https://feeds.feedburner.com/ettoday/dalemon/"),
                Map.entry("lifestyle", "https://feeds.feedburner.com/ettoday/lifestyle"),
                Map.entry("family", "https://feeds.feedburner.com/ettoday/family"),
                Map.entry("health", "https://feeds.feedburner.com/ettoday/health"),
                Map.entry("society", "https://feeds.feedburner.com/ettoday/society"),
                Map.entry("charity", "https://feeds.feedburner.com/ettoday/charity"),
                Map.entry("local", "https://feeds.feedburner.com/ettoday/local"),
                Map.entry("pet", "https://feeds.feedburner.com/ettoday/pet"),
                Map.entry("star", "https://feeds.feedburner.com/ettoday/star"),
                Map.entry("movies", "https://feeds.feedburner.com/ettoday/movies"),
                Map.entry("gender", "https://feeds.feedburner.com/ettoday/gender"),
                Map.entry("sport", "https://feeds.feedburner.com/ettoday/sport"),
                Map.entry("travel", "https://feeds.feedburner.com/ettoday/travel"),
                Map.entry("consuming", "https://feeds.feedburner.com/ettoday/consuming"),
                Map.entry("success", "https://feeds.feedburner.com/ettoday/success"),
                Map.entry("fashion", "https://feeds.feedburner.com/ettoday/fashion"),
                Map.entry("speed", "https://feeds.feedburner.com/ettoday/speed"),
                Map.entry("teck3c", "https://feeds.feedburner.com/ettoday/teck3c"),
                Map.entry("game", "https://feeds.feedburner.com/ettoday/game"),
                Map.entry("citizen", "https://feeds.feedburner.com/ettoday/citizen"),
                Map.entry("master", "https://feeds.feedburner.com/ettoday/master"),
                Map.entry("fortune", "https://feeds.feedburner.com/ettoday/fortune"),
                Map.entry("photo", "https://feeds.feedburner.com/ettoday/photo")
        );
    }
}
