package org.mini;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class HomePageData extends PageData {
    private List<String> titles;
    private List<String> links;

    public HomePageData(String html) {
        extractFromHtml(html);
    }

    @Override
    protected void extractFromHtml(String html) {
        Document mainPageDocument = Jsoup.parse(html);
        Elements aHrefs = mainPageDocument.select("a[href]");
        titles = aHrefs.subList(2, aHrefs.size() - 2).stream().map(Element::text).toList();
        links = aHrefs.subList(2, aHrefs.size() - 2).stream().map(element -> {
            return Configuration.Url + element.attr("href").substring(3);
        }).toList();
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getLinks() {
        return links;
    }
}
