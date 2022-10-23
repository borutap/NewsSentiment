package org.mini;

import java.util.*;

public class NewsCache {
    private final Map<String, ArticlePageData> articleTitleToPageDataMap;

    public NewsCache() {
        articleTitleToPageDataMap = new HashMap<>();
    }

    public boolean containsArticle(String articleTitle) {
        return articleTitleToPageDataMap.containsKey(articleTitle);
    }

    public ArticlePageData getArticlePageData(String articleTitle) {
        return articleTitleToPageDataMap.get(articleTitle);
    }

    public void addArticlePageData(String title, ArticlePageData articlePageData) {
        articleTitleToPageDataMap.put(title, articlePageData);
    }
}
