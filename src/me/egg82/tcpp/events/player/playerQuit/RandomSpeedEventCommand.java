package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.RandomSpeedRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Pair;
import ninja.egg82.plugin.commands.EventCommand;

public class RandomSpeedEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> randomSpeedRegistry = ServiceLocator.getService(RandomSpeedRegistry.class);
	
	//constructor
	public RandomSpeedEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!randomSpeedRegistry.hasRegister(uuid)) {
			return;
		}
		
		Pair<Float, Float> originalSpeed = randomSpeedRegistry.getRegister(uuid, Pair.class);
		player.setWalkSpeed(originalSpeed.getLeft());
		player.setFlySpeed(originalSpeed.getRight());
		randomSpeedRegistry.removeRegister(uuid);
	}
}
