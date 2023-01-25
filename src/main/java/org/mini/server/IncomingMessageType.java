package org.mini.server;

public enum IncomingMessageType {
    HELLO_SERVER,
    LOAD_TITLES,
    RELOAD_TITLES,
    SERIALIZE,
    LOAD_ARTICLE,   // LOAD_ARTICLE-indexOfArticle
    LOAD_SENTIMENT; // LOAD_SENTIMENT-indexOfArticle

    public static IncomingMessageType fromString(String text) {
        for (IncomingMessageType b : IncomingMessageType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        if (text.startsWith("LOAD_ARTICLE-")) {
            return LOAD_ARTICLE;
        }
        if (text.startsWith("LOAD_SENTIMENT-")) {
            return LOAD_SENTIMENT;
        }
        return null;
    }
}
