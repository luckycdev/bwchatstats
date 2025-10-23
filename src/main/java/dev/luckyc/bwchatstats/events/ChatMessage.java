package dev.luckyc.bwchatstats.events;

import dev.luckyc.bwchatstats.brain.brain;
import dev.luckyc.bwchatstats.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatMessage {
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (ConfigHandler.configToggled) {//TODO move farther out to not even check incoming msgs?
            if (!ConfigHandler.configAPIKey.isEmpty()) {//TODO also check if key is invalid and just stop and send a client side msg
                String msg = event.message.getUnformattedText();

                if (msg.equals("     Protect your bed and destroy the enemy beds.")) { // start of the game // TODO i might want to find a new method, lowcraw doesnt work bc i cant tell when it starts. i could use scoreboard
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000); // 2 sec delay bc of lag - only need about 0.5 sec but i need to fix the lag first
                        } catch (InterruptedException ignored) {
                        }

                        Minecraft.getMinecraft().addScheduledTask(() -> {
                            new brain().calculate();
                        });
                    }).start();
                }
            }
        }
    }
}