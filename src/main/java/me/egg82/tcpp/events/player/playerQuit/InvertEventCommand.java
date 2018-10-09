package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import me.egg82.tcpp.registries.InvertRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class InvertEventCommand extends EventHandler<PlayerQuitEvent> {
    //vars
    private IVariableRegistry<UUID> invertRegistry = ServiceLocator.getService(InvertRegistry.class);

    //constructor
    public InvertEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!invertRegistry.hasRegister(uuid)) {
            return;
        }

        player.setFlySpeed(Math.abs(player.getFlySpeed()));

        invertRegistry.removeRegister(uuid);
    }
}
