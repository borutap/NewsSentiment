package org.mini.server;

import org.mini.LogManager;
import org.mini.NewsParser;
import org.mini.PublicLogger;
import org.mini.sentiment.NaturalLanguageProcessor;
import org.mini.sentiment.SentimentCache;
import org.mini.sentiment.SentimentData;

public class IncomingMessageHandler {
    private final PublicLogger log;
    private NewsParser newsParser;
    private final SentimentCache sentimentCache;

    public IncomingMessageHandler() {
        log = LogManager.getPublicLogger();
        sentimentCache = new SentimentCache();
    }

    public Response handle(String message) {
        try {
            var messageType = IncomingMessageType.fromString(message);
            if (messageType == null) {
                throw new Exception(String.format("Can't handle message '%s'", message));
            }
            log.log(String.format("Handling '%s' message...", message));
            return switch (messageType) {
                case HELLO_SERVER -> new Response("Hello client!", ResponseMessageType.INFO);
                case LOAD_TITLES -> handleLoadTitlesMessage();
                case RELOAD_TITLES -> handleReloadTitlesMessage();
                case SERIALIZE -> handleSerializeMessage();
                case LOAD_ARTICLE -> handleLoadArticleMessage(getIndex(message));
                case LOAD_SENTIMENT -> handleLoadSentiment(getIndex(message));
            };
        } catch (Exception e) {
            return new Response(e.getMessage(), ResponseMessageType.INFO);
        }
    }

    private static int getIndex(String message) {
        return Integer.parseInt(message.substring(message.indexOf('-') + 1));
    }

    private Response handleSerializeMessage() throws Exception {
        sentimentCache.serialize();
        return new Response("Sentiment cache serialized", ResponseMessageType.INFO);
    }

    private Response handleReloadTitlesMessage() throws Exception {
        newsParser.reloadHomePage();
        return new Response(newsParser.getTitles(), ResponseMessageType.DATA_TITLES);
    }

    private Response handleLoadSentiment(int index) throws Exception {
        String articleTitle = newsParser.getArticleTitleAtIndex(index);
        SentimentData sentimentData;
        if (sentimentCache.containsSentimentData(articleTitle)) {
            log.log("Getting cached sentiment data");
            sentimentData = sentimentCache.getSentimentData(articleTitle);
        } else {
            log.log("Calculating sentiment");
            sentimentData = NaturalLanguageProcessor.getSentimentData(
                    newsParser.getArticleText(index));
            log.log("Caching sentiment data");
            sentimentCache.saveSentimentData(articleTitle, sentimentData);
        }
        return new Response(sentimentData, ResponseMessageType.DATA_SENTIMENT);
    }

    private Response handleLoadTitlesMessage() throws Exception {
        loadNewsParser();
        return new Response(newsParser.getTitles(), ResponseMessageType.DATA_TITLES);
    }

    private Response handleLoadArticleMessage(int index) throws Exception {
        return new Response(newsParser.getArticleParagraphs(index), ResponseMessageType.DATA_ARTICLE);
    }

    private void loadNewsParser() throws Exception {
        if (newsParser != null) {
            log.log("Using already initialized news parser");
            return;
        } 
        log.log("Initializing news parser");
        try {
            newsParser = new NewsParser();
        } catch (Exception e) {
            throw new Exception("Could not initialize news parser: " + e.getMessage());
        }
    }
}
