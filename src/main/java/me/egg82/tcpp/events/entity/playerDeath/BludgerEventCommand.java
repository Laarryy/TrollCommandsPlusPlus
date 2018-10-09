package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.BludgerRegistry;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class BludgerEventCommand extends EventHandler<PlayerDeathEvent> {
    //vars
    private IVariableRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);

    //constructor
    public BludgerEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        UUID uuid = event.getEntity().getUniqueId();

        if (!bludgerRegistry.hasRegister(uuid)) {
            return;
        }

        bludgerRegistry.getRegister(uuid, Fireball.class).remove();
        bludgerRegistry.removeRegister(uuid);
    }
}
