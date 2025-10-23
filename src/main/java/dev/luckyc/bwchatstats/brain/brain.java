package dev.luckyc.bwchatstats.brain;

import com.google.gson.JsonObject;
import dev.luckyc.bwchatstats.api.HypixelAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;

import java.util.*;

public class brain {

    public static Map<String, List<String>> getTeams() {
        Minecraft mc = Minecraft.getMinecraft();

        Map<String, List<String>> teams = new HashMap<>();
        teams.put("green", new ArrayList<>());
        teams.put("red", new ArrayList<>());
        teams.put("blue", new ArrayList<>());
        teams.put("yellow", new ArrayList<>());
        teams.put("white", new ArrayList<>());
        teams.put("gray", new ArrayList<>());
        teams.put("pink", new ArrayList<>());
        teams.put("aqua", new ArrayList<>());

        String localPlayer = mc.thePlayer.getName();

        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            String username = info.getGameProfile().getName();

            String prefix = "";
            if (info.getPlayerTeam() != null && info.getPlayerTeam().getColorPrefix() != null) {
                prefix = info.getPlayerTeam().getColorPrefix();
            }
            String cleanPrefix = prefix.replace("§", "");
            char colorPrefix = !cleanPrefix.isEmpty() ? cleanPrefix.charAt(0) : ' ';

            switch (colorPrefix) {
                case 'a': teams.get("green").add(username); break;
                case 'c': teams.get("red").add(username); break;
                case '9': teams.get("blue").add(username); break;
                case 'e': teams.get("yellow").add(username); break;
                case 'f': teams.get("white").add(username); break;
                case '8': teams.get("gray").add(username); break;
                case 'd': teams.get("pink").add(username); break;
                case 'b': teams.get("aqua").add(username); break;
            }
        }

        // remove empty teams and the team the player is on
        Iterator<Map.Entry<String, List<String>>> iterator = teams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            List<String> players = entry.getValue();
            if (players.isEmpty() || players.contains(localPlayer)) {
                iterator.remove();
            }
        }

        return teams;
    }

    public void calculate() {
        Map<String, List<String>> teams = getTeams();
        HypixelAPI api = new HypixelAPI();

        for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
            String teamName = entry.getKey();
            List<String> players = entry.getValue();

            int totalStars = 0;
            int totalFK = 0;
            int totalFD = 0;
            int totalWins = 0;
            int totalLosses = 0;
            int highestWS = 0;

            for (String playerName : players) {
                JsonObject stats = api.getPlayerData(playerName);
                if (stats != null) {
                    totalStars += stats.get("star").getAsInt();
                    totalFK += stats.get("fk").getAsInt();
                    totalFD += stats.get("fd").getAsInt();
                    totalWins += stats.get("wins").getAsInt();
                    totalLosses += stats.get("losses").getAsInt();

                    int ws = stats.get("ws").getAsInt();
                    if (ws > highestWS) highestWS = ws;
                }
            }

            double fkdr = totalFD == 0 ? totalFK : ((double) totalFK / totalFD);
            double wlr = totalLosses == 0 ? totalWins : ((double) totalWins / totalLosses);

            StringBuilder message = new StringBuilder();
            message.append(teamName.toUpperCase()).append(" TEAM: ")
                    .append("Stars: ").append(totalStars)
                    .append(", FKDR: ").append(String.format("%.2f", fkdr))
                    .append(", WLR: ").append(String.format("%.2f", wlr));

            if (highestWS > 50) {
                message.append(", WS: ").append(highestWS);
            }

            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText(message.toString())//TODO send to party chat
            );
        }
    }
//TODO function to check if theyre nicked (if 0.00 stats then (/w name)
}