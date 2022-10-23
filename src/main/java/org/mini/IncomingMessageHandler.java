package org.mini;

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
            if (message.equals("LOAD_TITLES")) {
                log.log("Handling 'LOAD_TITLES' message...");
                return handleLoadTitlesMessage();
            } else if (message.equals("RELOAD_TITLES")) {
                log.log("Handling 'RELOAD_TITLES' message...");
                return handleReloadTitlesMessage();
            } else if (message.startsWith("LOAD_ARTICLE-")) {
                log.log(String.format("Handling '%s' message...", message));

                int index = Integer.parseInt(message.substring(message.indexOf('-') + 1));

                return handleLoadArticleMessage(index);
            } else if (message.startsWith("LOAD_SENTIMENT-")) {
                log.log(String.format("Handling '%s' message please be patient it may take some time...", message));

                int index = Integer.parseInt(message.substring(message.indexOf('-') + 1));

                return handleLoadSentiment(index);
            } else {
                throw new Exception(String.format("Can't handle message '%s'", message));
            }
        } catch (Exception e) {
            return new Response(e.getMessage(), MESSAGE_TYPE.INFO);
        }
    }

    private Response handleReloadTitlesMessage() throws Exception {
        newsParser.reloadHomePage();
        return new Response(newsParser.getTitles(), MESSAGE_TYPE.DATA_TITLES);
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
        return new Response(sentimentData, MESSAGE_TYPE.DATA_SENTIMENT);
    }

    private Response handleLoadTitlesMessage() throws Exception {
        loadNewsParser();
        return new Response(newsParser.getTitles(), MESSAGE_TYPE.DATA_TITLES);
    }

    private Response handleLoadArticleMessage(int index) throws Exception {
        return new Response(newsParser.getArticleParagraphs(index), MESSAGE_TYPE.DATA_ARTICLE);
    }

    private void loadNewsParser() throws Exception {
        if (newsParser == null) {
            log.log("Initializing news parser");
            try {
                newsParser = new NewsParser();
            } catch (Exception e) {
                throw new Exception("Could not initialize news parser: " + e.getMessage());
            }
        } else {
            log.log("Using already initialized news parser");
        }
    }
}
