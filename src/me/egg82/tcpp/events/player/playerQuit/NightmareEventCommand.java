package me.egg82.tcpp.events.player.playerQuit;

import java.util.ArrayDeque;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NightmareEventCommand extends EventCommand {
	//vars
	private IRegistry nightmareRegistry = (IRegistry) ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerQuitEvent e = (PlayerQuitEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!nightmareRegistry.hasRegister(uuid)) {
			return;
		}
		
		nightmareRegistry.setRegister(uuid, ArrayDeque.class, null);
	}
}
