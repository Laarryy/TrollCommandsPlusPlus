package me.egg82.tcpp.events.player.playerQuit;

import java.util.List;

import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.FoolsGoldChunkRegistry;
import me.egg82.tcpp.services.FoolsGoldRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FoolsGoldEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
	private IRegistry foolsGoldChunkRegistry = ServiceLocator.getService(FoolsGoldChunkRegistry.class);
	
	//constructor
	public FoolsGoldEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String uuid = event.getPlayer().getUniqueId().toString();
		
		foolsGoldRegistry.setRegister(uuid, List.class, null);
		foolsGoldChunkRegistry.setRegister(uuid, List.class, null);
	}
}
