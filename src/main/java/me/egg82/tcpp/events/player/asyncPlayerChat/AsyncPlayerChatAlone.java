package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatAlone implements Consumer<AsyncPlayerChatEvent> {
    public AsyncPlayerChatAlone() { }

    public void accept(AsyncPlayerChatEvent event) {
        Set<UUID> set = CollectionProvider.getSet("alone");

        Set<Player> recipients = event.getRecipients();
        Set<Player> removedPlayers = new HashSet<>();

        if (set.contains(event.getPlayer().getUniqueId())) {
            for (Player p : recipients) {
                if (p.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                    continue;
                }
                removedPlayers.add(p);
            }
        }

        for (Player p : recipients) {
            if (set.contains(p.getUniqueId()) && !p.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                removedPlayers.add(p);
            }
        }

        recipients.removeAll(removedPlayers);
    }
}
