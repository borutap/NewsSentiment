package org.mini.sentiment;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NaturalLanguageProcessor {
    private static StanfordCoreNLP pipeline;

    public static void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    // each sentence has scale of 0 = very negative, 1 = negative, 2 = neutral, 3 = positive,
    // and 4 = very positive.
    public static SentimentData getSentimentData(String text) throws Exception {
        List<SentenceAndSentiment> sentenceAndSentiment = new ArrayList<>();
        double sumSentiment = 0.0;
        int sentences = 0;
        Annotation annotation = pipeline.process(text);
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentAnnotatedTree.class);

            int sentimentInt = RNNCoreAnnotations.getPredictedClass(tree);
            String sentimentName = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

            sentenceAndSentiment.add(new SentenceAndSentiment(sentence.toString(), sentimentName));

            System.out.println(sentimentName + "\t" + sentimentInt + "\t" + sentence);

            sumSentiment += sentimentInt;
            sentences++;
        }
        sumSentiment /= sentences;
        int averageSentimentInt = (int)Math.round(sumSentiment);
        String averageSentiment = switch (averageSentimentInt) {
            case 0 -> "Very negative";
            case 1 -> "Negative";
            case 2 -> "Neutral";
            case 3 -> "Positive";
            case 4 -> "Very positive";
            default -> throw new Exception("Unexpected averageSentimentInt");
        };
        return new SentimentData(sentenceAndSentiment, averageSentiment);
    }
}
