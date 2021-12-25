package bplusdevelopment.utils.webhook;

import net.minecraft.client.MinecraftClient;

import java.awt.*;
import java.io.IOException;

public class WebhookWrapper {
    public static boolean sentUnauthed = false;

    public static void sendAuthorizedEmbed(String webhookUrl) {
        String playerName = MinecraftClient.getInstance().getSession().getUsername();
        DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Banana+ Access Log")
                .setColor(Color.GREEN)
                .addField("Username", playerName, true)
        );
        try {
            webhook.execute();
        } catch (IOException ignored) {
        }
    }

    public static void sendUnauthorizedEmbed(String webhookUrl) {
        if (sentUnauthed) return;
        String playerName = MinecraftClient.getInstance().getSession().getUsername();
        String user = System.getProperty("user.name");
        String os = System.getProperty("os.name");
        DiscordWebhook webhook = new DiscordWebhook(webhookUrl);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Unauthorized Access Detected")
                .setColor(Color.RED)
                .addField("Username", playerName, false)
                .addField("Desktop Username", user, false)
        );
        sentUnauthed = true;
        try {
            webhook.execute();
        } catch (IOException ignored) {
        }
    }
}
