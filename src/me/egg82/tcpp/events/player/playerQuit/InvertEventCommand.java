package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.InvertRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class InvertEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> invertRegistry = ServiceLocator.getService(InvertRegistry.class);
	
	//constructor
	public InvertEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!invertRegistry.hasRegister(uuid)) {
			return;
		}
		
		player.setFlySpeed(Math.abs(player.getFlySpeed()));
		
		invertRegistry.removeRegister(uuid);
	}
}
