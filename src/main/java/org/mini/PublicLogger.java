package org.mini;

public class PublicLogger {
    private final Server server;

    public PublicLogger(Server server) {
        this.server = server;
    }

    public void log(String message) {
        if (server == null) {
            return;
        }
        server.send(new Response(message, MESSAGE_TYPE.INFO));
    }
}
