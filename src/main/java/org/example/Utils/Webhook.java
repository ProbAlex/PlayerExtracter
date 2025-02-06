package org.example.Utils;

import static spark.Spark.*;

public class Webhook {
    private final int port;
    private final WebhookListener listener;

    // Constructor accepts the port and a listener
    public Webhook(int port, WebhookListener listener) {
        this.port = port;
        this.listener = listener;
    }

    // Start the server
    public void start() {
        port(this.port);  // Set the server to listen on the specified port

        // Handle /webhook POST requests
        post("/webhook", (req, res) -> {
            String payload = req.body();  // Get the request body
            listener.onWebhookReceived(payload);  // Trigger the callback
            res.status(200);
            return "Webhook received";
        });

        System.out.println("Webhook server running on port " + this.port);
    }
    
}
