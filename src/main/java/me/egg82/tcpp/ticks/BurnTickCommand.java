package me.egg82.tcpp.ticks;

import java.util.UUID;

import me.egg82.tcpp.registries.BurnRegistry;
import org.bukkit.entity.Player;

import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class BurnTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> burnRegistry = ServiceLocator.getService(BurnRegistry.class);

    //constructor
    public BurnTickCommand() {
        super(0L, 40L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : burnRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        player.setFireTicks(50);
    }
}
