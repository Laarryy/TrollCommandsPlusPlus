package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import me.egg82.tcpp.reflection.entity.IFakeLivingEntity;
import me.egg82.tcpp.registries.NightmareRegistry;
import org.bukkit.event.player.PlayerRespawnEvent;

import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class NightmareEventCommand extends EventHandler<PlayerRespawnEvent> {
    //vars
    private IRegistry<UUID, IConcurrentSet<IFakeLivingEntity>> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);

    //constructor
    public NightmareEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        UUID uuid = event.getPlayer().getUniqueId();

        IConcurrentSet<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid);

        if (entities != null) {
            for (IFakeLivingEntity e : entities) {
                e.teleportTo(event.getRespawnLocation());
            }
        }
    }
}
