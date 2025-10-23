package dev.luckyc.bwchatstats.api;

import dev.luckyc.bwchatstats.config.ConfigHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HypixelAPI {

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

            JsonObject bedwarsStats = json.getAsJsonObject("player")
                    .getAsJsonObject("stats")
                    .getAsJsonObject("Bedwars");

            int star = playerStats.getAsJsonObject("achievements").get("bedwars_level").getAsInt();

            int finalKills = bedwarsStats.get("final_kills_bedwars").getAsInt();
            int finalDeaths = bedwarsStats.get("final_deaths_bedwars").getAsInt();

            int wins = bedwarsStats.get("wins_bedwars").getAsInt();
            int losses = bedwarsStats.get("losses_bedwars").getAsInt();

            int winstreak = bedwarsStats.get("winstreak").getAsInt();

            JsonObject result = new JsonObject();
            result.addProperty("star", star);
            result.addProperty("fk", finalKills);
            result.addProperty("fd", finalDeaths);
            result.addProperty("wins", wins);
            result.addProperty("losses", losses);
            result.addProperty("ws", winstreak);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
