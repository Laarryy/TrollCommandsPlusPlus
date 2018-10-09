package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import me.egg82.tcpp.registries.ConvertRegistry;
import org.bukkit.event.player.PlayerQuitEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ConvertEventCommand extends EventHandler<PlayerQuitEvent> {
    //vars
    private IVariableRegistry<UUID> convertRegistry = ServiceLocator.getService(ConvertRegistry.class);

    //constructor
    public ConvertEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        convertRegistry.removeRegister(event.getPlayer().getUniqueId());
    }
}
