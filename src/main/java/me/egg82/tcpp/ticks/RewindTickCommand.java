package me.egg82.tcpp.ticks;

import java.util.UUID;

import me.egg82.tcpp.registries.RewindRegistry;
import org.bukkit.entity.Player;

import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class RewindTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> rewindRegistry = ServiceLocator.getService(RewindRegistry.class);

    //constructor
    public RewindTickCommand() {
        super(0L, 5L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : rewindRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        player.setPlayerTime(player.getPlayerTime() - 100L, false);
    }
}
