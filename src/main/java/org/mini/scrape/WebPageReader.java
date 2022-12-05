package org.mini.scrape;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebPageReader {
    private final HttpClient client;

    public WebPageReader() {
        client = HttpClient.newHttpClient();
    }

    public String getPageBody(String URL) throws Exception {
        var request = createRequest(URL);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            String message = String.format(
                    "Error making a request to page %s:%s%s", URL, System.lineSeparator(), e.getMessage()
            );
            throw new Exception(message);
        }
    }

    private HttpRequest createRequest(String URL) {
        return HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
    }
}
