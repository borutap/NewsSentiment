package org.mini;

import java.util.HashMap;
import java.util.Map;

public class SentimentCache {
    private final Map<String, SentimentData> articleTitleToSentimentDataMap;

    public SentimentCache() {
        articleTitleToSentimentDataMap = new HashMap<>();
    }

    public boolean containsSentimentData(String articleTitle) {
        return articleTitleToSentimentDataMap.containsKey(articleTitle);
    }

    public void saveSentimentData(String articleTitle, SentimentData data) {
        articleTitleToSentimentDataMap.put(articleTitle, data);
    }

    public SentimentData getSentimentData(String articleTitle) {
        return articleTitleToSentimentDataMap.get(articleTitle);
    }
}
