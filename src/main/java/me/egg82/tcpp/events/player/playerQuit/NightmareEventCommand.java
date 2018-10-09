package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import me.egg82.tcpp.reflection.entity.IFakeLivingEntity;
import me.egg82.tcpp.registries.NightmareRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class NightmareEventCommand extends EventHandler<PlayerQuitEvent> {
    //vars
    private IRegistry<UUID, IConcurrentSet<IFakeLivingEntity>> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);

    //constructor
    public NightmareEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        IConcurrentSet<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid);
        if (entities != null) {
            for (IFakeLivingEntity e : entities) {
                e.removeVisibilityFromPlayer(player);
            }
        }
    }
}
