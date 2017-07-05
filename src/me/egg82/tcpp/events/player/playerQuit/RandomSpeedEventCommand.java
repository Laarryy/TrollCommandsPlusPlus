package me.egg82.tcpp.events.player.playerQuit;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.RandomSpeedRegistry;
import me.egg82.tcpp.services.RandomSpeedSaveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.Pair;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class RandomSpeedEventCommand extends EventCommand {
	//vars
	private IRegistry randomSpeedRegistry = (IRegistry) ServiceLocator.getService(RandomSpeedRegistry.class);
	private IRegistry randomSpeedSaveRegistry = (IRegistry) ServiceLocator.getService(RandomSpeedSaveRegistry.class);
	
	//constructor
	public RandomSpeedEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		PlayerQuitEvent e = (PlayerQuitEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!randomSpeedRegistry.hasRegister(uuid)) {
			return;
		}
		
		randomSpeedRegistry.setRegister(uuid, Player.class, null);
		Pair<Float, Float> originalSpeed = (Pair<Float, Float>) randomSpeedSaveRegistry.getRegister(uuid);
		player.setWalkSpeed(originalSpeed.getLeft());
		player.setFlySpeed(originalSpeed.getRight());
		randomSpeedSaveRegistry.setRegister(uuid, Pair.class, null);
	}
}
