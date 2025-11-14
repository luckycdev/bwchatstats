package dev.luckyc.bwchatstats.api;

import dev.luckyc.bwchatstats.config.ConfigHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HypixelAPI {

    private int safeInt(JsonObject object, String value) {
        return object.has(value) && !object.get(value).isJsonNull() ? object.get(value).getAsInt() : 0; //checks if the json object actually has a value and not just null
    }

    public JsonObject getPlayerData(String name) {
        try {
            URL url = new URL("https://api.hypixel.net/v2/player?name=" + name + "&key=" + ConfigHandler.configAPIKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            connection.disconnect();

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(content.toString()).getAsJsonObject();

            JsonObject playerStats = json.getAsJsonObject("player");

            if (playerStats == null || playerStats.isJsonNull()) {
                return null; // not found, maybe nicked //TODO add real nick check with /w
            }
            if (!playerStats.has("stats") ||
                    !playerStats.getAsJsonObject("stats").has("Bedwars")) {
                return null;
            }

            JsonObject bedwarsStats = playerStats
                    .getAsJsonObject("stats")
                    .getAsJsonObject("Bedwars");

            int star = safeInt(playerStats.getAsJsonObject("achievements"), "bedwars_level");

            int finalKills = safeInt(bedwarsStats, "final_kills_bedwars");
            int finalDeaths = safeInt(bedwarsStats, "final_deaths_bedwars");
            double fkdr = finalDeaths == 0 ? finalKills : ((double) finalKills / finalDeaths);

            int wins = safeInt(bedwarsStats, "wins_bedwars");
            int losses = safeInt(bedwarsStats, "losses_bedwars");
            double wlr = losses == 0 ? wins : ((double) wins / losses);

            int winstreak = safeInt(bedwarsStats, "winstreak");

            JsonObject result = new JsonObject();
            result.addProperty("star", star);
            result.addProperty("fkdr", fkdr);
            result.addProperty("wlr", wlr);
            result.addProperty("ws", winstreak);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}