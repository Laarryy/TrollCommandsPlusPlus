package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import me.egg82.tcpp.reflection.block.IFakeBlockHelper;
import me.egg82.tcpp.registries.LsdRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.ThreadUtil;

public class LsdEventCommand extends EventHandler<PlayerRespawnEvent> {
    //vars
    private IRegistry<UUID, IConcurrentSet<Location>> lsdRegistry = ServiceLocator.getService(LsdRegistry.class);
    private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);

    //constructor
    public LsdEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (lsdRegistry.hasRegister(uuid)) {
            IConcurrentSet<Location> bLocs = lsdRegistry.getRegister(uuid);
            ThreadUtil.submit(new Runnable() {
                public void run() {
                    for (Location l : bLocs) {
                        fakeBlockHelper.deque(l);
                    }
                }
            });

            bLocs.clear();
        }
    }
}
