package org.mini.sentiment;

import java.io.Serializable;

public record SentenceAndSentiment(String sentence, String sentiment) implements Serializable {}
