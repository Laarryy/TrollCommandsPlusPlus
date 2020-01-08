package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import java.util.UUID;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.services.player.PlayerVisibilityHandler;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class AloneTroll extends BukkitTroll {
    private final Plugin plugin;
    private PlayerVisibilityHandler playerVisibilityHandler;

    public AloneTroll(Plugin plugin, UUID playerID) throws InstantiationException, IllegalAccessException, ServiceNotFoundException {
        super(playerID, TrollType.ALONE);
        this.plugin = plugin;
        playerVisibilityHandler = ServiceLocator.get(PlayerVisibilityHandler.class);

        events.add(
                BukkitEvents.subscribe(plugin, PlayerJoinEvent.class, EventPriority.NORMAL)
                        .handler(this::playerJoin)
        );
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            playerVisibilityHandler.hide(plugin, player, p);
        }

        issuer.sendInfo(Message.ALONE__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);

        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            playerVisibilityHandler.show(plugin, player, p);
        }

        issuer.sendInfo(Message.ALONE__STOP, "{player}", player.getName());
    }

    private void playerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getUniqueId().equals(playerID)) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                playerVisibilityHandler.hide(plugin, event.getPlayer(), p);
            }
        } else {
            Player player = Bukkit.getPlayer(playerID);
            if (player == null) {
                return;
            }
            playerVisibilityHandler.hide(plugin, player, event.getPlayer());
        }
    }
}
