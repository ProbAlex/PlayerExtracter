package org.example.Utils;

@FunctionalInterface
public interface WebhookListener {
    void onWebhookReceived(String payload);  // The method that will be triggered on receiving the webhook
}

