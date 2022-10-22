package org.mini;

import java.util.List;

public class NewsParser {

    private final WebPageReader reader;
    private final HomePageData homePageData;
    private ArticlePageData articlePageData;
    private int articlePageIndex = -1;
    private final PublicLogger log;

    public NewsParser() throws Exception {
        log = LogManager.getPublicLogger();
        reader = new WebPageReader();

        log.log(String.format("Requesting %s body", Configuration.Url));
        String homePageHtml = reader.getPageBody(Configuration.Url);

        log.log("Parsing home page");
        homePageData = new HomePageData(homePageHtml);
    }

    public List<String> getTitles() {
        return homePageData.getTitles();
    }

    public List<String> getArticleParagraphs(int index) throws Exception {
        if (articlePageIndex == index) {
            log.log("Getting cached article text");
        } else {
            log.log(String.format("Getting article at index %d", index));
            parseArticle(index);
        }
        return articlePageData.getParagraphs();
    }

    private void parseArticle(int index) throws Exception {
        var articleLink = homePageData.getLinks().get(index);
        log.log(String.format("Requesting %s body", articleLink));
        String articlePageHtml = reader.getPageBody(articleLink);
        log.log("Parsing article");
        articlePageData = new ArticlePageData(articlePageHtml);
        articlePageIndex = index;
    }

    public String getArticleText() throws Exception {
        // it should be stateless
        if (articlePageIndex == -1) {
            throw new Exception("You have not viewed any article yet");
        }
        return articlePageData.getArticleText();
    }
}
