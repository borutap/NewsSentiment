package org.mini.sentiment;

import org.mini.Configuration;
import org.mini.LogManager;
import org.mini.PublicLogger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SentimentCache {
    private final Map<String, SentimentData> articleTitleToSentimentDataMap;
    private final PublicLogger publicLogger;

    public SentimentCache() {
        articleTitleToSentimentDataMap = deserializeIfExists();
        publicLogger = LogManager.getPublicLogger();
    }

    public void serialize() throws Exception {
        if (articleTitleToSentimentDataMap.isEmpty()) {
            throw new Exception("No sentiment data to serialize");
        }
        try
        {
            publicLogger.log(String.format(
                    "Serializing sentiment data from %d articles...", articleTitleToSentimentDataMap.size()));
            FileOutputStream fileOutputStream = new FileOutputStream(Configuration.CacheFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(articleTitleToSentimentDataMap);
            objectOutputStream.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not serialize: " + ex.getMessage());
        }
    }

    private Map<String, SentimentData> deserializeIfExists() {
        try {
            FileInputStream fileInputStream = new FileInputStream(Configuration.CacheFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            var ret = (Map<String, SentimentData>) objectInputStream.readObject();
            objectInputStream.close();
            return ret;
        } catch (FileNotFoundException ex) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
