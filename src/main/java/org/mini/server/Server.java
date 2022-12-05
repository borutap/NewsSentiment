package org.mini.server;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {
    private WebSocket lastConnection;
    private IncomingMessageHandler messageHandler;
    private final Gson gson;

    public Server(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        gson = new Gson();
    }

    public void send(Response response) {

        if (lastConnection == null) {
            return;
        }
        lastConnection.send(gson.toJson(response));
    }

    private String infoInJson(String message) {
        return gson.toJson(new Response(message, ResponseMessageType.INFO));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        lastConnection = conn;
        // This method sends a message to the new client
        conn.send(infoInJson("Welcome to the server!"));
        // This method sends a message to all clients connected
        broadcast(infoInJson("New connection: " + conn.getRemoteSocketAddress()));
        System.out.println(
                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " is in!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Response response = messageHandler.handle(message);
        conn.send(gson.toJson(response));
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        messageHandler = new IncomingMessageHandler();
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}