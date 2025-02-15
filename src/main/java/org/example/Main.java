package org.example;
import org.example.Utils.Pricing;
import org.example.Utils.Webhook;
import org.example.Utils.WebhookListener;
import org.example.Utils.WebsocketNames;
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
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.HashMap;



public class Main {
    private static String API_KEY; // Your Hypixel API key
    private static Pricing PRICING;

    //{"success":true,"auctions":[{"_id":"67a4081e0cf2c232f3df1067","uuid":"c46b5d7b2a664554b4e1c260dd2917e5","auctioneer":"f64d98b6f69249559d8b491923cb6f4d","profile_id":"f64d98b6f69249559d8b491923cb6f4d","coop":["f64d98b6f69249559d8b491923cb6f4d"],"start":1738803230855,"end":1738803246915,"item_name":"Auspicious Gemstone Gauntlet","item_lore":"�8Breaking Power 8\n\n�7Damage: �c+300\n�7Strength: �c+50\n�7Defense: �a+14 �d(+14)\n�7Intelligence: �a+12 �d(+12)\n�7Mining Speed: �a+950 �9(+75) �d(+75)\n�7Pristine: �a+3.7 �d(+1.2)\n�7Mining Fortune: �a+90 �9(+20) �d(+25)\n�7Mining Wisdom: �a+10\n �9[�a?�9] �5[�6?�5] �9[�e?�9] �9[�b?�9] �9[�5?�9]\n\n�9�d�lFlowstate III\n�7Consecutive blocks broken grant\n�7�6+3? Mining Speed�7. Stops after �a10s\n�a�7of not mining and caps at �a200 �7blocks.\n�9Compact X\n�7Gain �3+10? Mining Wisdom �7and a �a0.57%\n�a�7chance to drop an enchanted item.\n�9Efficiency V\n�7Increases how quickly your tool\n�7breaks blocks.\n�9Fortune IV\n�7Grants �6+45? Mining Fortune�7, which\n�7increases your chance for multiple\n�7drops.\n�9Prismatic V\n�7Grants �5+2.5 �5? Pristine�7, which\n�7increases the chance to improve the\n�7quality of dropped �dGemstones�7.\n\n�7�6Ability: Reduced To Atoms\n�7Mobs killed on �bMining Islands �7drop the same\n�7�dGemstones �7as your filled Gemstone Slots.\n�7�8(2s cooldown)\n�7\n�7�6Ability: Kinetic\n�7Killing mobs on �bMining Islands �7reduces your\n�7�6Forge Timers �7by �a0s�7.\n�7�8(+0.5s per Perfect Gemstone)\n\n�9Auspicious Bonus\n�7Grants �6+0.9% �6? Mining Fortune�7.\n\n�d�l�ka�r �d�lMYTHIC GAUNTLET �d�l�ka","extra":"Auspicious Gemstone Gauntlet Skull Item Efficiency Fortune Compact Prismatic �d�lFlowstate","category":"misc","tier":"MYTHIC","starting_bid":26000000,"item_bytes":{"type":0,"data":"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"},"claimed":false,"claimed_bidders":[],"bin":true,"highest_bid_amount":26000000,"bids":[{"auction_id":"c46b5d7b2a664554b4e1c260dd2917e5","bidder":"9f8f80c9060f4ecb82bdf1210f4e6a4b","profile_id":"3d7013de1761481183f915243393c480","amount":26000000,"timestamp":1738803250905}]}]}
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Hello! Input your Hypixel Api Key: ");
        API_KEY = scan.nextLine();

        //define a listener to be triggered on every webhook receive.
        WebhookListener listener = Main::onWebhookReceived;
        // Create and start the Webhook server on port 6969
        Webhook webhook = new Webhook(6969, listener);
        webhook.start();

        WebsocketNames SOCKET = new WebsocketNames();
        PRICING = new Pricing(API_KEY, SOCKET);

        String jsonData = "{\n" +
                "      \"_id\": \"63a46ada0cf23c3995b56ff0\",\n" +
                "      \"uuid\": \"7671d05cf14a47f4b889edff980b3977\",\n" +
                "      \"auctioneer\": \"ac4ef4682a034fb9bb9db7bf55f4c8ad\",\n" +
                "      \"profile_id\": \"77c4496bdda84c9dbf5d748c7cf09c15\",\n" +
                "      \"coop\": [\n" +
                "        \"ac4ef4682a034fb9bb9db7bf55f4c8ad\"\n" +
                "      ],\n" +
                "      \"start\": 1671719642803,\n" +
                "      \"end\": 1671741242803,\n" +
                "      \"item_name\": \"Fierce Strong Dragon Chestplate\",\n" +
                "      \"item_lore\": \"§7Strength: §c+35 §9(+10)\\n§7Crit Chance: §c+6% §9(+6%)\\n§7Crit Damage: §c+18% §9(+18%)\\n§7Health: §a+203 §e(+8)\\n§7Defense: §a+184 §e(+4)\\n §8[§7❁§8]\\n\\n§9Growth V\\n§7Grants §a+75 §c❤ Health§7.\\n§9Protection V\\n§7Grants §a+20 §a❈ Defense§7.\\n§9Thorns III\\n§7Grants a §a50% §7chance to\\n§7rebound §a9% §7of damage dealt\\n§7back at the attacker.\\n\\n§6Full Set Bonus: Strong Blood\\n§7Improves §9Aspect of the End\\n§8⋗ §c+75 Damage\\n\\n§7Instant Transmission:\\n§8⋗ §a+2 §7teleport range\\n§8⋗ §a+3 §7seconds\\n§8⋗ §c+5❁ Strength §7on cast\\n\\n§7§4❣ §cRequires §aCombat Skill 16§c.\\n§6§lLEGENDARY CHESTPLATE\",\n" +
                "      \"extra\": \"Fierce Strong Dragon Chestplate Leather Chestplate Thorns Growth Protection\",\n" +
                "      \"category\": \"armor\",\n" +
                "      \"tier\": \"LEGENDARY\",\n" +
                "      \"starting_bid\": 1200000,\n" +
                "      \"item_bytes\": {\n" +
                "        \"type\": 0,\n" +
                "        \"data\": \"H4sIAAAAAAAAAFVU3W7jRBQ+abbFyQLlAokVu1oG6EobZd2142Rs5y5N0jRSaasmICG0qsb2OLHqeMLMZH8uuUPiAbjqFUh9j74Ft3kQxBknLeCb+fnOd36+c8Z1gBpUsjoAVHZgJ0sqzQrs9sWq0JU6VDWb1eARL+I5mK8Cj78vIsnZNYtyXqlC7SRL+HHOZgrRv+vwUZKpZc4+IOlUSG7h7VN4dnfrT7TkxUzPu+TuNm56HVzCl03XacBXiPZlpkl/zoqYbwzoi40BfdGAr+8NBmzBZlsDN9ha4KYBX6LJCWf5xj9rthwPV/6yGTTgKWIDnvJC8Q3oBu0N2G7APu6Cn9BiffML7t5gvo/R7UiKd3pOfoBvEBpJVmhVUn2Td7y++ZNsoiF6CPtIuJBC81hnokDSt/8ntRyzrG9+Jds0StYnyJrOhSwUGY/HpQpbDjPmHcfU58elJkSL0qnkETYmMXhYwiIlSSkKSUw+8ATvIhZfE6aJnnNcNJ64PMS6MAI9XuU5mXBNjkSxUl2CXRHFjBzlQiRgHI4XSynecpN42FNLLIlgDONqWCTwOUq0/u33sgEoxaYf6NqEHRdKY/pkikWoRaYUatGF5w8M1MEE0DznSyE1QTPkfvEf3LTMVzwWRaJMuQ+hOtgccj9AZdkFiZnSGPkVnu5u2+ubP4zlJf95lckye9YXiwhVmFxnWLJLET00Y4Kb/HQ4Gp4Nepc/kv7JcDK9OO1Nh1XYjUUuJPz1vGfBozO24Kb59DjjEhuwFWog2QyD9+dcaRxzzaEO+8P3WrKe1jKLVpqrKnw2F/pqKTTT4io2LwkT3bHuA9Rart/1nC7tWLAXCaXGCRzQJKQxo4nthB612ymN7DBNfdvnfkBdz225Ld+CulqydwVPjtHNQTvywzDxWjZN4hQpLWpH1IvsIODc53HstdqeBdZCJFmKRcBeWpZiQU3IbJYVUzaD2tH5ZHI1uDy/sMzbhyeT6eX52QhveqPzs6t/5anDx+YngA1ecJzRKuzpcnaxsioeZuV7wcNuFerLh7dgLgDVXK3Q90EnoqmbOo7tpphmOw1CO/CYb3eckEY8oQ5jMWanswWqyxZL+NR/7b5uuSTodijpfYciwt7DzME/ncU5s7oEAAA=\"\n" +
                "      },\n" +
                "      \"claimed\": false,\n" +
                "      \"claimed_bidders\": [],\n" +
                "      \"highest_bid_amount\": 0,\n" +
                "      \"bids\": []\n" +
                "    }";
        try {
            System.out.println(removeUnicode(convertToNBT(jsonData)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(getPlayerName(extractAuctioneer(getSkyblockAuction(API_KEY, UUID))));
    }

    // The custom method to be triggered when the webhook is received
    public static void onWebhookReceived(String payload) {
        // {"username":"TPM+","avatar_url":"https://mc-heads.net/head/93c55ee2e7d04ca5b93b6a3cbe9bf600.png","content":"","embeds":[{"title":"Flip Found","color":9929727,"fields":[{"name":"","value":"Stonks: [`? Heroic Spirit Sceptre ?????`](https://sky.coflnet.com/a/4600a73427194c9ca3b1a7ed07a33782) `25.0M` ? `30.2M` (`4.3M` profit) [NUGGET] `2` volume"}],"thumbnail":{"url":"https://mc-heads.net/head/93c55ee2e7d04ca5b93b6a3cbe9bf600.png"},"footer":{"text":"TPM Rewrite - Found by Stonks","icon_url":"https://media.discordapp.net/attachments/1303439738283495546/1304912521609871413/3c8b469c8faa328a9118bddddc6164a3.png?ex=67311dfd&is=672fcc7d&hm=8a14479f3801591c5a26dce82dd081bd3a0e5c8f90ed7e43d9140006ff0cb6ab&=&format=webp&quality=lossless&width=888&height=888"}}]}
        try {
            String Player = resolvePlayer(payload);
            if (Player != null) {
                PRICING.checkPlayer(resolvePlayer(payload));
            }
        } finally {
        }
    }

    public static String extractUUID(String payload) {
        JSONObject rawPost = new JSONObject(payload);
        JSONArray embedsArray = rawPost.getJSONArray("embeds");
        JSONObject firstEmbed = embedsArray.getJSONObject(0);
        JSONArray fieldsArray = firstEmbed.getJSONArray("fields");
        JSONObject firstField = fieldsArray.getJSONObject(0);
        String fieldValue = firstField.getString("value");


        String regex = "(?<=https://sky.coflnet.com/a/)([^)]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fieldValue);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String resolvePlayer(String payload) {
        return formatUUID(extractAuctioneer(getSkyblockAuction(API_KEY, extractUUID(payload))));
    }

    public static String getSkyblockAuction(String apiKey, String uuid) {
        try {
            String urlString = "https://api.hypixel.net/v2/skyblock/auction" + "?uuid=" + uuid;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("API-Key", apiKey);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String extractAuctioneer(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray auctions = jsonObject.getJSONArray("auctions");
            if (!auctions.isEmpty()) {
                return auctions.getJSONObject(0).getString("auctioneer");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getPlayerName(String uuid) {
        try {
            // Remove dashes from UUID
            String trimmedUuid = uuid.replace("-", "");

            String urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + trimmedUuid;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("name"); // Extract player name
            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String formatUUID(String uuid) {
        return uuid.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        );
    }

    public static String convertToNBT(String jsonData) throws IOException {
        // Step 1: Parse the JSON to extract item_bytes.data
        JSONObject jsonObject = new JSONObject(jsonData);
        String base64CompressedData = jsonObject.getJSONObject("item_bytes").getString("data");

        // Step 2: Decode the Base64 String
        byte[] decodedData = Base64.getDecoder().decode(base64CompressedData);

        // Step 3: Decompress the Data (GZIP)
        GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(decodedData));

        // Step 4: Read the decompressed data
        String decompressedData = readDecompressedData(gzipInputStream);

        // Step 5: Create a JSON object with the decompressed data as "jsonNbt"
        JSONObject result = new JSONObject();
        result.put("jsonNbt", decompressedData);

        return result.toString();
    }

    // Example of processing decompressed data into a simple string (you can customize this)
    private static String readDecompressedData(GZIPInputStream gzipInputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int data;
        while ((data = gzipInputStream.read()) != -1) {
            stringBuilder.append((char) data);
        }
        return stringBuilder.toString();
    }

    public static String removeUnicode(String input){
        String regex = "\\\\u[a-fA-F0-9]{4}";
        return input.replaceAll(regex, "");
    }
}