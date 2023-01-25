package org.mini.server;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Dictionary;
import java.util.Hashtable;

public class Server extends WebSocketServer {
    private final Dictionary<Thread, WebSocket> connections;
    private IncomingMessageHandler messageHandler;
    private final Gson gson;

    public Server(int port) {
        super(new InetSocketAddress(port));
        connections = new Hashtable<>();
        gson = new Gson();
    }

    public void send(Thread thread, Response response) {
        WebSocket connection = connections.get(thread);
        if (connection == null) {
            return;
        }
        connection.send(gson.toJson(response));
    }

    private String infoInJson(String message) {
        return gson.toJson(new Response(message, ResponseMessageType.INFO));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
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
        connections.put(Thread.currentThread(), conn);
        Response response = messageHandler.handle(message);
        conn.send(gson.toJson(response));
        System.out.println(conn + ": " + message);
        connections.remove(Thread.currentThread());
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