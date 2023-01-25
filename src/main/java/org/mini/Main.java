package org.mini;

import org.mini.sentiment.NaturalLanguageProcessor;
import org.mini.server.Server;

public class Main {
    public static void main(String[] args) {
        int port = 8887;
        Server server;
        server = new Server(port);

        LogManager.setPublicLogger(new PublicLogger(server));
        NaturalLanguageProcessor.init();

        server.start();
        System.out.println("Server started on port: " + server.getPort());
    }
}