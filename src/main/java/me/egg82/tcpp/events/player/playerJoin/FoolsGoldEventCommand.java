package me.egg82.tcpp.events.player.playerJoin;

import java.util.UUID;

import me.egg82.tcpp.registries.FoolsGoldRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.util.FoolsGoldHelper;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class FoolsGoldEventCommand extends EventHandler<PlayerJoinEvent> {
    //vars
    private IVariableRegistry<UUID> foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
    private FoolsGoldHelper foolsGoldHelper = ServiceLocator.getService(FoolsGoldHelper.class);

    //constructor
    public FoolsGoldEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (foolsGoldRegistry.hasRegister(uuid)) {
            TaskUtil.runAsync(new Runnable() {
                public void run() {
                    foolsGoldHelper.updatePlayer(uuid, player, player.getLocation());
                }
            }, 80L);
        }
    }
}
