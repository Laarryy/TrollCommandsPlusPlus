package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class AsyncPlayerChatAmnesia implements Consumer<AsyncPlayerChatEvent> {
    private final Plugin plugin;
    private final Random rand = new Random();

    public AsyncPlayerChatAmnesia(Plugin plugin) {
        this.plugin = plugin;
    }

    public void accept(AsyncPlayerChatEvent event) {
        Set<UUID> set = CollectionProvider.getSet("amnesia");

        for (Iterator<Player> i = event.getRecipients().iterator(); i.hasNext(); ) {
            Player p = i.next();

            if (set.contains(p.getUniqueId())) {
                if (Math.random() <= 0.05d) {
                    // Remove
                    i.remove();
                } else {
                    if (Math.random() <= 0.2d) {
                        // Delay
                        sendMessage(p, String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()), event.isAsynchronous(), rand.nextInt(81) + 20L);
                        i.remove();
                    }
                    if (Math.random() <= 0.1d) {
                        // Repeat
                        sendMessage(p, String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()), event.isAsynchronous(), rand.nextInt(81) + 20L);
                    }
                }
            }
        }
    }

    private void sendMessage(Player player, String message, boolean async, long delay) {
        if (async) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> player.sendMessage(message), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.sendMessage(message), delay);
        }
    }
}
