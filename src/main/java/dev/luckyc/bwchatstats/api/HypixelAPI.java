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
            double fkdr = finalDeaths == 0 ? finalKills : ((double) finalKills / finalDeaths);

            int wins = bedwarsStats.get("wins_bedwars").getAsInt();
            int losses = bedwarsStats.get("losses_bedwars").getAsInt();
            double wlr = losses == 0 ? wins : ((double) wins / losses);

            int winstreak = bedwarsStats.get("winstreak").getAsInt();

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
