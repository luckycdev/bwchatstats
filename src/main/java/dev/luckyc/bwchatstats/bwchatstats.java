package dev.luckyc.bwchatstats;

import dev.luckyc.bwchatstats.commands.bwcsCommand;
import dev.luckyc.bwchatstats.config.ConfigHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = bwchatstats.MODID,
        name = bwchatstats.MODNAME,
        version = bwchatstats.VERSION)
public class bwchatstats {

    public static final String MODID = "bwchatstats";
    public static final String MODNAME = "Bedwars Chat Stats";
    public static final String VERSION = "0.4";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new bwcsCommand());

        MinecraftForge.EVENT_BUS.register(new dev.luckyc.bwchatstats.events.ChatMessage());
    }
}
