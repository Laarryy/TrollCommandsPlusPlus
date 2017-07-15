package me.egg82.tcpp.events.player.playerQuit;

import java.util.ArrayDeque;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NightmareEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!nightmareRegistry.hasRegister(uuid)) {
			return;
		}
		
		nightmareRegistry.setRegister(uuid, ArrayDeque.class, null);
	}
}
