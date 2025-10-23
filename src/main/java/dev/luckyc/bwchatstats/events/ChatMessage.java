package dev.luckyc.bwchatstats.events;

import dev.luckyc.bwchatstats.brain.brain;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatMessage {
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();

        if (msg.equals("     Protect your bed and destroy the enemy beds.")) { // start of the game // TODO i might want to find a new method, lowcraw doesnt work bc i cant tell when it starts
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // 1 sec later
                } catch (InterruptedException ignored) {}

                Minecraft.getMinecraft().addScheduledTask(() -> {
                    new brain().calculate();
                });
            }).start();
        }
    }
}
