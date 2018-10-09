package me.egg82.tcpp.events.entity.entityDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.LuckyChickenRegistry;
import me.egg82.tcpp.registries.LuckyVillagerRegistry;
import org.bukkit.event.entity.EntityDeathEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class LuckyEventCommand extends EventHandler<EntityDeathEvent> {
    //vars
    private IVariableExpiringRegistry<UUID> luckyChickenRegistry = ServiceLocator.getService(LuckyChickenRegistry.class);
    private IVariableExpiringRegistry<UUID> luckyVillagerRegistry = ServiceLocator.getService(LuckyVillagerRegistry.class);

    //constructor
    public LuckyEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        UUID uuid = event.getEntity().getUniqueId();

        luckyChickenRegistry.removeRegister(uuid);
        luckyVillagerRegistry.removeRegister(uuid);
    }
}
