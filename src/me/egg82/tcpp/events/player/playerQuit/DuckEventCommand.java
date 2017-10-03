package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.DuckRegistry;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DuckEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> duckRegistry = ServiceLocator.getService(DuckRegistry.class);
	
	private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
	
	//constructor
	public DuckEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!duckRegistry.hasRegister(uuid)) {
			return;
		}
		
		disguiseHelper.undisguise(player);
		duckRegistry.removeRegister(uuid);
	}
}
