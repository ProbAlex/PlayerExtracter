package org.example.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Resolver {

    private static String extractUUID(String payload) {
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

    public static String resolvePlayer(String payload, String API_KEY) {
        return formatUUID(extractAuctioneer(getSkyblockAuction(API_KEY, extractUUID(payload))));
    }

    private static String getSkyblockAuction(String apiKey, String uuid) {
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

    private static String extractAuctioneer(String jsonResponse) {
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

    @Deprecated
    private static String getPlayerName(String uuid) {
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

    private static String formatUUID(String uuid) {
        return uuid.replaceFirst(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"
        );
    }

}
