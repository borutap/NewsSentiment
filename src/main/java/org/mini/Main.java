package org.mini;

import org.mini.sentiment.NaturalLanguageProcessor;
import org.mini.server.Server;

import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {
        int port = 8887;
        Server server;
        try {
            server = new Server(port);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        LogManager.setPublicLogger(new PublicLogger(server));
        NaturalLanguageProcessor.init();

        server.start();
        System.out.println("Server started on port: " + server.getPort());
    }
}