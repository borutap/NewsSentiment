package org.mini.scrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mini.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HomePageData extends PageData {
    private List<String> titles;
    private List<String> links;

    public HomePageData(String html) {
        super(html);
    }

    @Override
    protected void extractFromHtml(String html) {
        Document mainPageDocument = Jsoup.parse(html);
        Elements allAHrefs = mainPageDocument.select("a[href]");
        // Some Elements are junk, omit them
        List<Element> aHrefs = allAHrefs
                .subList(2, allAHrefs.size() - 2)
                .stream()
                .toList();
        titles = aHrefs.stream()
                .map(Element::text)
                .toList();
        links = aHrefs.stream()
                .map(element -> buildArticleUrl(element.attr("href")))
                .toList();
    }

    private String buildArticleUrl(String href) {
        return Configuration.NewsSourceUrl + href;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getLinks() {
        return links;
    }
}
