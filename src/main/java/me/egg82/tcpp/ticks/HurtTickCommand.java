package me.egg82.tcpp.ticks;

import java.util.UUID;

import me.egg82.tcpp.registries.HurtRegistry;
import org.bukkit.entity.Player;

import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class HurtTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> hurtRegistry = ServiceLocator.getService(HurtRegistry.class);

    //constructor
    public HurtTickCommand() {
        super(0L, 15L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : hurtRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        player.damage(1.0d);
    }
}
