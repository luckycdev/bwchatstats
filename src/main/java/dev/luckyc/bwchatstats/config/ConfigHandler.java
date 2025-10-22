package dev.luckyc.bwchatstats.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    private static Configuration config;

    public static boolean configToggled;
    public static String configAPIKey;

    // reads the config file and loads the values from
    public static void loadConfig(File file) {
        config = new Configuration(file);
        config.load();
        configToggled = config.get("Toggled", "toggled", true, "Toggle Bedwars Chat Stats").getBoolean();
        configAPIKey = config.get("APIKey", "APIKey", "", "Hypixel API key").getString();
    }

    // you want to call the saveConfig() when you change the values of the fields via code
    // and want to have the changes saved to the config file
    public static void saveConfig() {
        config.get("Toggled", "toggled", true, "Toggle Bedwars Chat Stats").setValue(configToggled);
        config.get("APIKey", "APIKey", "", "Hypixel API key").setValue(configAPIKey);
        if (config.hasChanged()) {
            config.save();
        }
    }

}
