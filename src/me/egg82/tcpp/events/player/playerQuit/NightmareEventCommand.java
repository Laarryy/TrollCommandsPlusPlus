package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NightmareEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		nightmareRegistry.removeRegister(event.getPlayer().getUniqueId());
	}
}
