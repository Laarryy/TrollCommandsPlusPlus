package me.egg82.tcpp.ticks;

import java.util.UUID;

import me.egg82.tcpp.registries.SpinRegistry;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class SpinTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> spinRegistry = ServiceLocator.getService(SpinRegistry.class);

    //constructor
    public SpinTickCommand() {
        super(0L, 5L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : spinRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key), spinRegistry.getRegister(key, Float.class));
        }
    }

    private void e(Player player, float speed) {
        if (player == null) {
            return;
        }

        Location newLocation = player.getLocation().clone();
        float newYaw = newLocation.getYaw() + speed;

        while (newYaw < 0.0f) {
            newYaw += 360.0f;
        }
        while (newYaw >= 360.0f) {
            newYaw -= 360.0f;
        }

        newLocation.setYaw(newYaw);
        player.teleport(newLocation);
    }
}
