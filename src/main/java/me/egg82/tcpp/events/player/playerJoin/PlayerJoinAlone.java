package me.egg82.tcpp.events.player.playerJoin;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.services.player.PlayerVisibilityHandler;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerJoinAlone implements Consumer<PlayerJoinEvent> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Plugin plugin;

    private PlayerVisibilityHandler playerVisibilityHandler;

    public PlayerJoinAlone(Plugin plugin) {
        this.plugin = plugin;

        try {
            playerVisibilityHandler = ServiceLocator.get(PlayerVisibilityHandler.class);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void accept(PlayerJoinEvent event) {
        Set<UUID> set = CollectionProvider.getSet("alone");

        if (set.contains(event.getPlayer().getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                playerVisibilityHandler.hide(plugin, event.getPlayer(), p);
            }
        }

        for (UUID u : set) {
            Player player = Bukkit.getPlayer(u);
            if (player == null) {
                continue;
            }

            playerVisibilityHandler.hide(plugin, player, event.getPlayer());
        }
    }
}
