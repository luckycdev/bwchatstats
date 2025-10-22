package dev.luckyc.bwchatstats.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    private static Configuration config;

    public static boolean configToggled;
    public static String configAPIKey;

    public static void loadConfig(File file) {
        config = new Configuration(file);
        config.load();
        configToggled = config.get("Toggled", "toggled", true, "Toggle Bedwars Chat Stats").getBoolean();
        configAPIKey = config.get("APIKey", "APIKey", "", "Hypixel API key").getString();
    }

    public static void saveConfig() {
        config.get("Toggled", "toggled", true, "Toggle Bedwars Chat Stats").setValue(configToggled);
        config.get("APIKey", "APIKey", "", "Hypixel API key").setValue(configAPIKey);
        if (config.hasChanged()) {
            config.save();
        }
    }

}
