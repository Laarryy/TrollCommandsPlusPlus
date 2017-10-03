package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.DisplayLocationRegistry;
import me.egg82.tcpp.services.registries.DisplayRegistry;
import me.egg82.tcpp.util.DisplayHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> displayRegistry = ServiceLocator.getService(DisplayRegistry.class);
	private IRegistry<UUID> displayLocationRegistry = ServiceLocator.getService(DisplayLocationRegistry.class);
	
	private DisplayHelper displayHelper = ServiceLocator.getService(DisplayHelper.class);
	
	//constructor
	public DisplayEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!displayRegistry.hasRegister(uuid)) {
			return;
		}
		
		displayHelper.unsurround(displayLocationRegistry.getRegister(uuid, Location.class));
		displayRegistry.removeRegister(uuid);
		displayLocationRegistry.removeRegister(uuid);
	}
}
