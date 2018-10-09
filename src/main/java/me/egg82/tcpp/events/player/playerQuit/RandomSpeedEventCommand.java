package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import me.egg82.tcpp.registries.RandomSpeedRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.patterns.tuples.pair.FloatFloatPair;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class RandomSpeedEventCommand extends EventHandler<PlayerQuitEvent> {
    //vars
    private IVariableRegistry<UUID> randomSpeedRegistry = ServiceLocator.getService(RandomSpeedRegistry.class);

    //constructor
    public RandomSpeedEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!randomSpeedRegistry.hasRegister(uuid)) {
            return;
        }

        FloatFloatPair originalSpeed = randomSpeedRegistry.getRegister(uuid, FloatFloatPair.class);
        player.setWalkSpeed(originalSpeed.getLeft());
        player.setFlySpeed(originalSpeed.getRight());
        randomSpeedRegistry.removeRegister(uuid);
    }
}
