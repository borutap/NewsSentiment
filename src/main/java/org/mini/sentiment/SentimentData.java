package org.mini.sentiment;

import java.io.Serializable;
import java.util.List;

public record SentimentData(List<SentenceAndSentiment> list, String average) implements Serializable {}
