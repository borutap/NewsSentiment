package org.mini;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ArticlePageData extends PageData {
    private String articleText;
    private List<String> paragraphs;

    public ArticlePageData(String html) {
        extractFromHtml(html);
    }

    @Override
    protected void extractFromHtml(String html) {
        Document articleDocument = Jsoup.parse(html);
        // p html elements without the id attribute
        Elements pElements = articleDocument.select("p:not([id])");

        List<String> pStrings = new ArrayList<>();
        // last 3 elements are junk from the site
        for (int i = 0; i < pElements.size() - 3; i++) {
            String text = pElements.get(i).text();
            if (!text.isEmpty()) {
                pStrings.add(text);
            }
        }

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
