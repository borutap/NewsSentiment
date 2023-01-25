package org.mini.scrape;

public abstract class PageData {
    public PageData(String html) {
        extractFromHtml(html);
    }

    protected abstract void extractFromHtml(String html);
}
