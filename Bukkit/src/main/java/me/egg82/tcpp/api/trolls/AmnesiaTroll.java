package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public class AmnesiaTroll extends BukkitTroll {
    private final Plugin plugin;
    private final Random rand = new Random();

    public AmnesiaTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);
        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        events.add(
                BukkitEvents.subscribe(plugin, AsyncPlayerChatEvent.class, EventPriority.HIGH)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::playerChat)
        );

        issuer.sendInfo(Message.AMNESIA__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);

        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        issuer.sendInfo(Message.AMNESIA__STOP, "{player}", player.getName());
    }

    private void playerChat(AsyncPlayerChatEvent event) {
        for (Iterator<Player> i = event.getRecipients().iterator(); i.hasNext(); ) {
            Player p = i.next();

            if (playerID.equals(p.getUniqueId())) {
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
