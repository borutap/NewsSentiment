package org.mini;

import java.util.List;

public class NewsParser {
    private final WebPageReader reader;
    private HomePageData homePageData;
    private final PublicLogger log;
    private final NewsCache newsCache;

    public NewsParser() throws Exception {
        log = LogManager.getPublicLogger();
        reader = new WebPageReader();
        newsCache = new NewsCache();

        loadHomePage();
    }

    public List<String> getTitles() {
        return homePageData.getTitles();
    }

    public List<String> getArticleParagraphs(int index) throws Exception {
        ArticlePageData articlePageData;
        String articleTitle = getArticleTitleAtIndex(index);
        if (newsCache.containsArticle(articleTitle)) {
            log.log("Getting cached article paragraphs");
            articlePageData = newsCache.getArticlePageData(articleTitle);
        } else {
            log.log(String.format("Getting article at index %d", index));
            articlePageData = parseArticle(index);
            log.log("Caching article");
            newsCache.addArticlePageData(articleTitle, articlePageData);
        }
        return articlePageData.getParagraphs();
    }

    public String getArticleTitleAtIndex(int index) {
        return homePageData.getTitles().get(index);
    }

    public String getArticleText(int index) throws Exception {
        String articleTitle = getArticleTitleAtIndex(index);
        if (newsCache.containsArticle(articleTitle)) {
            return newsCache.getArticlePageData(articleTitle).getArticleText();
        } else {
            throw new Exception("News cache does not have this article but it should as article text is viewed");
        }
    }

    private ArticlePageData parseArticle(int index) throws Exception {
        var articleLink = homePageData.getLinks().get(index);
        log.log(String.format("Requesting %s body", articleLink));
        String articlePageHtml = reader.getPageBody(articleLink);
        log.log("Parsing article");
        return new ArticlePageData(articlePageHtml);
    }

    public void reloadHomePage() throws Exception {
        loadHomePage();
    }

    private void loadHomePage() throws Exception {
        log.log(String.format("Requesting %s body", Configuration.Url));
        String homePageHtml = reader.getPageBody(Configuration.Url);

        log.log("Parsing home page");
        homePageData = new HomePageData(homePageHtml);
    }
}
