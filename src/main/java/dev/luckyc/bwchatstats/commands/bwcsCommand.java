package dev.luckyc.bwchatstats.commands;

import dev.luckyc.bwchatstats.bwchatstats;
import dev.luckyc.bwchatstats.config.ConfigHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class bwcsCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "bwchatstats";
    }

    @Override
    public List<String> getCommandAliases() { return new ArrayList<>(Arrays.asList("bwcs", "bedwarschatstats")); }//TODO allow caps

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // return true otherwise you won't be able to use the command
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {//TODO when "/bwcs aaaa" is run nothing is shown - it should probably show /bwcs help output
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.addChatMessage(new ChatComponentText("§c---- Bedwars §fChat Stats §bby luckycdev §c----"));//TODO doesnt line up perfectly
            sender.addChatMessage(new ChatComponentText("§6- §f/bwcs help"));
            sender.addChatMessage(new ChatComponentText("§6- §f/bwcs toggle"));
            sender.addChatMessage(new ChatComponentText("§6- §f/bwcs setapikey §e<key>"));
            sender.addChatMessage(new ChatComponentText("§6- §f/bwcs getpaikey"));
            sender.addChatMessage(new ChatComponentText("§c------------§f--------------§c------------"));
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            if (!ConfigHandler.configToggled && ConfigHandler.configAPIKey.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("§7[§cBW§fCS§7]§cl API key not set! §cRun §f/bwcs setapikey §e<key> §cfirst"));
                return;
            }

            ConfigHandler.configToggled = !ConfigHandler.configToggled;
            ConfigHandler.saveConfig();

            String toggledWord;
            if (ConfigHandler.configToggled) {
                toggledWord = "§aon";
            } else {
                toggledWord = "§coff";
            }

            ConfigHandler.saveConfig();

            String toggledState = ConfigHandler.configToggled ? "§aON" : "§cOFF";
            sender.addChatMessage(new ChatComponentText(
                    "§7[§cBW§fCS§7]§f Bedwars Chat Stats toggled " + toggledState
            ));
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("setapikey")) {
            if (args.length < 2) {
                sender.addChatMessage(new ChatComponentText("§7[§cBW§fCS§7]§f Usage: /bwcs setapikey §e<key>"));
                return;
            }

            ConfigHandler.configAPIKey = args[1];
            ConfigHandler.saveConfig();
            sender.addChatMessage(new ChatComponentText("§7[§cBW§fCS§7]§f API key set to: §e" + args[1])); //TODO click to copy
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("getapikey")) {
            if (ConfigHandler.configAPIKey.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("§7[§cBW§fCS§7]§c§l API key not set! §cRun §f/bwcs setapikey §e<key> §cfirst"));
            }
            else {
                sender.addChatMessage(new ChatComponentText("§7[§cBW§fCS§7]§f API Key: §e" + ConfigHandler.configAPIKey)); //TODO click to copy
            }
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
