package dev.luckyc.bwchatstats.brain;

import com.google.gson.JsonObject;
import dev.luckyc.bwchatstats.api.HypixelAPI;

import dev.luckyc.bwchatstats.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

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
                case 'a' :
                    teams.get("green").add(username);
                    break;
                case 'c' :
                    teams.get("red").add(username);
                    break;
                case '9' :
                    teams.get("blue").add(username);
                    break;
                case 'e' :
                    teams.get("yellow").add(username);
                    break;
                case 'f' :
                    teams.get("white").add(username);
                    break;
                case '8' :
                    teams.get("gray").add(username);
                    break;
                case 'd' :
                    teams.get("pink").add(username);
                    break;
                case 'b' :
                    teams.get("aqua").add(username);
                    break;
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
        Multithreading.runAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {} // wait 500ms for tab to fill
            Map<String, List<String>> teams = getTeams();
            HypixelAPI api = new HypixelAPI();

            List<String> teamMessages = new ArrayList<>();

            for (Map.Entry<String, List<String>> entry : teams.entrySet()) {
                String teamName = entry.getKey();
                List<String> players = entry.getValue();

                int totalStars = 0;
                double combinedFKDR = 0;
                double combinedWLR = 0;
                int highestWS = 0;

                for (String playerName : players) {
                    JsonObject stats = api.getPlayerData(playerName);
                    if (stats != null) {
                        totalStars += stats.get("star").getAsInt();
                        combinedFKDR += stats.get("fkdr").getAsDouble();
                        combinedWLR += stats.get("wlr").getAsDouble();

                        int ws = stats.get("ws").getAsInt();
                        if (ws > highestWS) highestWS = ws;
                    }
                }

                StringBuilder message = new StringBuilder();
                message.append(teamName.toUpperCase()).append(" TEAM: ")
                        .append("Stars: ").append(totalStars)
                        .append(", FKDR: ").append(String.format("%.2f", combinedFKDR))
                        .append(", WLR: ").append(String.format("%.2f", combinedWLR));

                if (highestWS > 50) {
                    message.append(", WS: ").append(highestWS);
                }

                teamMessages.add(message.toString());
            }
            for (String msg : teamMessages) {
                Minecraft.getMinecraft().addScheduledTask(() ->
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + msg)//TODO order and say who to target
                );

                try {
                    Thread.sleep(500); // 0.5 sec delay
                } catch (InterruptedException ignored) {
                }
            }
        });
    }
//TODO function to check if theyre nicked (if 0.00 stats then /w name)
}