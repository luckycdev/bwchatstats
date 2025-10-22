package dev.luckyc.bwchatstats.commands;

import dev.luckyc.bwchatstats.config.ConfigHandler;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class bwcsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "bedwarschatstats";
    }

    @Override
    public List<String> getCommandAliases() { return Collections.singletonList("bwcs"); } // TODO is Collections.singletonList best option for single item list?

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // return true otherwise you won't be able to use the command
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {//TODO when "/bwcs aaaa" is run nothing is shown
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) { //TODO colors
            sender.addChatMessage(new ChatComponentText("Bedwars Chat Stats by luckyc"));
            sender.addChatMessage(new ChatComponentText("/bwcs help"));
            sender.addChatMessage(new ChatComponentText("/bwcs toggle"));
            sender.addChatMessage(new ChatComponentText("/bwcs setapikey <key>"));
            sender.addChatMessage(new ChatComponentText("/bwcs getpaikey"));
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            ConfigHandler.configToggled = !ConfigHandler.configToggled;
            ConfigHandler.saveConfig();

            String toggledWord;
            if(ConfigHandler.configToggled) {
                toggledWord = "on";
            }
            else {
                toggledWord = "off";
            }

            sender.addChatMessage(new ChatComponentText("[BWCS] Bedwars Chat Stats toggled " + toggledWord));
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("setapikey")) {
            if (args.length < 2) {
                sender.addChatMessage(new ChatComponentText("[BWCS] Usage: /bwcs setapikey <key>"));
                return;
            }

            ConfigHandler.configAPIKey = args[1];
            ConfigHandler.saveConfig();
            sender.addChatMessage(new ChatComponentText("[BWCS] API key set to: " + args[1]));

        } else if (args.length > 0 && args[0].equalsIgnoreCase("getapikey")) {

            sender.addChatMessage(new ChatComponentText("[BWCS] API Key: " + ConfigHandler.configAPIKey)); //TODO click to copy

        } else if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            if (ConfigHandler.configAPIKey.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("[BWCS] API Key is empty! Run /bwcs setapikey <key>"));
            } else {
                try {
                    URL url = new URL("https://api.hypixel.net/v2/skyblock/news?key=" + ConfigHandler.configAPIKey);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    sender.addChatMessage(new ChatComponentText("Response Code: " + responseCode));

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();

                    connection.disconnect();

                    sender.addChatMessage(new ChatComponentText("Response Content: " + content.toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        // when you type the command and press tab complete,
        // this method will allow you to cycle through the words that match what you typed
        final String[] options = new String[]{"setapikey", "getapikey", "test"};
        return getListOfStringsMatchingLastWord(args, options);
    }

}
