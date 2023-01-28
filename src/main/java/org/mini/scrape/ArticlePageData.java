package org.mini.scrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ArticlePageData extends PageData {
    private String articleText;
    private List<String> paragraphs;

    public ArticlePageData(String html) {
        super(html);
    }

    @Override
    protected void extractFromHtml(String html) {
        Document articleDocument = Jsoup.parse(html);
        // p html elements without the id attribute
        Elements pElements = articleDocument.select("p.paragraph--lite");

        // Last element can be omitted
        List<String> pStrings = pElements.stream()
                .limit(pElements.size() - 1)
                .map(Element::text)
                .filter(text -> !text.isEmpty())
                .toList();

        paragraphs = pStrings;
        articleText = String.join(" ", pStrings);
    }

    public String getArticleText() {
        return articleText;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }
}
