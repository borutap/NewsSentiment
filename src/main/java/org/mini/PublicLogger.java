package org.mini;

import org.mini.server.Response;
import org.mini.server.ResponseMessageType;
import org.mini.server.Server;

public class PublicLogger {
    private final Server server;

    public PublicLogger(Server server) {
        this.server = server;
    }

    public void log(String message) {
        if (server == null) {
            return;
        }
        server.send(new Response(message, ResponseMessageType.INFO));
    }
}
