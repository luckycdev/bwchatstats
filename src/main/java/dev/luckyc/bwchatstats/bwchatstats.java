package dev.luckyc.bwchatstats;

import dev.luckyc.bwchatstats.commands.bwcsCommand;
import dev.luckyc.bwchatstats.config.ConfigHandler;
import dev.luckyc.bwchatstats.events.ExampleKeybindListener;
import dev.luckyc.bwchatstats.hud.ExampleHUD;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
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
    public static final String VERSION = "0.1";

    // this method is one entry point of you mod
    // it is called by forge when minecraft is starting
    // it is called before the other methods below
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // loads the config file from the disk
        ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // register your commands here
        ClientCommandHandler.instance.registerCommand(new bwcsCommand());

        // the ExampleKeybind has a method with the @SubscribeEvent annotation
        // for that code to run, that class needs to be registered on the MinecraftForge EVENT_BUS
        // register your other EventHandlers here
        MinecraftForge.EVENT_BUS.register(new ExampleKeybindListener());
        MinecraftForge.EVENT_BUS.register(new ExampleHUD());

        if (Loader.isModLoaded("patcher")) {
            // this code will only run if the mod with id "patcher" is loaded
            // you can use it to load or not while modules of your mod that depends on other mods
        }

    }
}
