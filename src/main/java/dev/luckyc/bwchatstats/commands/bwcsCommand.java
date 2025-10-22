package dev.luckyc.bwchatstats.commands;

import com.google.gson.JsonObject;
import dev.luckyc.bwchatstats.api.HypixelAPI;
import dev.luckyc.bwchatstats.config.ConfigHandler;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class bwcsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "bwchatstats";
    }

    @Override
    public List<String> getCommandAliases() { return new ArrayList<>(Arrays.asList("bwcs", "bedwarschatstats")); }

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
            if (ConfigHandler.configToggled) {
                toggledWord = "on";
            } else {
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
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("getapikey")) {
            sender.addChatMessage(new ChatComponentText("[BWCS] API Key: " + ConfigHandler.configAPIKey)); //TODO click to copy
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            if (args.length < 2) {
                sender.addChatMessage(new ChatComponentText("[BWCS] Usage: /bwcs test <name>"));
                return;
            }
            HypixelAPI api = new HypixelAPI();
            JsonObject result = api.getPlayerData(args[1]);
            sender.addChatMessage(new ChatComponentText("[BWCS] " + result.toString()));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        // when you type the command and press tab complete,
        // this method will allow you to cycle through the words that match what you typed
        final String[] options = new String[]{"help", "toggle", "setapikey", "getapikey"};
        return getListOfStringsMatchingLastWord(args, options);
    }

}
