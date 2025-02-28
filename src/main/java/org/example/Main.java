package org.example;
import org.example.Utils.*;
import org.json.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;


public class Main {
    private static String API_KEY; // Your Hypixel API key


    //{"success":true,"auctions":[{"_id":"67a4081e0cf2c232f3df1067","uuid":"c46b5d7b2a664554b4e1c260dd2917e5","auctioneer":"f64d98b6f69249559d8b491923cb6f4d","profile_id":"f64d98b6f69249559d8b491923cb6f4d","coop":["f64d98b6f69249559d8b491923cb6f4d"],"start":1738803230855,"end":1738803246915,"item_name":"Auspicious Gemstone Gauntlet","item_lore":"�8Breaking Power 8\n\n�7Damage: �c+300\n�7Strength: �c+50\n�7Defense: �a+14 �d(+14)\n�7Intelligence: �a+12 �d(+12)\n�7Mining Speed: �a+950 �9(+75) �d(+75)\n�7Pristine: �a+3.7 �d(+1.2)\n�7Mining Fortune: �a+90 �9(+20) �d(+25)\n�7Mining Wisdom: �a+10\n �9[�a?�9] �5[�6?�5] �9[�e?�9] �9[�b?�9] �9[�5?�9]\n\n�9�d�lFlowstate III\n�7Consecutive blocks broken grant\n�7�6+3? Mining Speed�7. Stops after �a10s\n�a�7of not mining and caps at �a200 �7blocks.\n�9Compact X\n�7Gain �3+10? Mining Wisdom �7and a �a0.57%\n�a�7chance to drop an enchanted item.\n�9Efficiency V\n�7Increases how quickly your tool\n�7breaks blocks.\n�9Fortune IV\n�7Grants �6+45? Mining Fortune�7, which\n�7increases your chance for multiple\n�7drops.\n�9Prismatic V\n�7Grants �5+2.5 �5? Pristine�7, which\n�7increases the chance to improve the\n�7quality of dropped �dGemstones�7.\n\n�7�6Ability: Reduced To Atoms\n�7Mobs killed on �bMining Islands �7drop the same\n�7�dGemstones �7as your filled Gemstone Slots.\n�7�8(2s cooldown)\n�7\n�7�6Ability: Kinetic\n�7Killing mobs on �bMining Islands �7reduces your\n�7�6Forge Timers �7by �a0s�7.\n�7�8(+0.5s per Perfect Gemstone)\n\n�9Auspicious Bonus\n�7Grants �6+0.9% �6? Mining Fortune�7.\n\n�d�l�ka�r �d�lMYTHIC GAUNTLET �d�l�ka","extra":"Auspicious Gemstone Gauntlet Skull Item Efficiency Fortune Compact Prismatic �d�lFlowstate","category":"misc","tier":"MYTHIC","starting_bid":26000000,"item_bytes":{"type":0,"data":"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"},"claimed":false,"claimed_bidders":[],"bin":true,"highest_bid_amount":26000000,"bids":[{"auction_id":"c46b5d7b2a664554b4e1c260dd2917e5","bidder":"9f8f80c9060f4ecb82bdf1210f4e6a4b","profile_id":"3d7013de1761481183f915243393c480","amount":26000000,"timestamp":1738803250905}]}]}
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Hello! Input your Hypixel Api Key: ");
        API_KEY = scan.nextLine();

        //define a listener to be triggered on every webhook receive.
        WebhookListener listener = Main::onWebhookReceived;
        Webhook webhook = new Webhook(6969, listener);
        webhook.start();

        WebsocketNames SOCKET = new WebsocketNames();
    }

    // The custom method to be triggered when the webhook is received
    public static void onWebhookReceived(String payload) {
            System.out.println(Resolver.resolvePlayer(payload,API_KEY));
    }


}