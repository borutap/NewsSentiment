package org.mini;

import java.util.List;

public record SentimentData(List<SentenceAndSentiment> list, String average) {}
