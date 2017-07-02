package me.egg82.tcpp.events.player.playerQuit;

import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.FoolsGoldRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FoolsGoldEventCommand extends EventCommand {
	//vars
	private IRegistry foolsGoldRegistry = (IRegistry) ServiceLocator.getService(FoolsGoldRegistry.class);
	private IRegistry foolsGoldChunkRegistry = (IRegistry) ServiceLocator.getService(FoolsGoldChunkRegistry.class);
	
	//constructor
	public FoolsGoldEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerQuitEvent e = (PlayerQuitEvent) event;
		
		String uuid = e.getPlayer().getUniqueId().toString();
		
		foolsGoldRegistry.setRegister(uuid, List.class, null);
		foolsGoldChunkRegistry.setRegister(uuid, List.class, null);
	}
}
