package org.example.Utils;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebsocketNames {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    private Server server;

    // Constructor to start the WebSocket server
    public WebsocketNames() {
        startServer();
    }

    // Start WebSocket server on port 7001
    private void startServer() {
        server = new Server(7001);

        // Create a servlet context handler
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // Map the WebSocket endpoint to /ws
        context.addServlet(WebSocketServletImpl.class, "/ws");

        server.setHandler(context);

        try {
            server.start();
            System.out.println("WebSocket server started on ws://localhost:7001/ws");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Push a message to all connected clients
    public void push(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getRemote().sendString(message);  // Send message to client
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Stop the WebSocket server
    public void stopServer() {
        if (server != null) {
            try {
                server.stop();
                System.out.println("WebSocket server stopped.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // WebSocket Servlet Implementation (no annotations)
    public static class WebSocketServletImpl extends WebSocketServlet {

        @Override
        public void configure(WebSocketServletFactory factory) {
            // Remove the problematic setPolicy() line
            factory.register(WebSocketHandlerImpl.class);  // Register handler class
        }
    }

    // WebSocket Handler Implementation (no annotations)
    public static class WebSocketHandlerImpl extends WebSocketAdapter {

        @Override
        public void onWebSocketConnect(Session session) {
            super.onWebSocketConnect(session);
            sessions.add(session);
            System.out.println("New connection: " + session.getRemoteAddress());
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
            System.out.println("Received: " + message);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
            sessions.remove(getSession());
            System.out.println("Connection closed: " + getSession().getRemoteAddress());
        }
    }
}
