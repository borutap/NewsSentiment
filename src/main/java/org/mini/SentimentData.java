package org.mini;

import java.util.List;

public class SentimentData {
    public List<SentenceAndSentiment> list;
    public String average;

    public SentimentData(List<SentenceAndSentiment> list, String average) {
        this.list = list;
        this.average = average;
    }
}
