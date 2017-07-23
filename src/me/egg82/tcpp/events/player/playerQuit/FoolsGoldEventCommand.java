package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.util.FoolsGoldHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FoolsGoldEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private FoolsGoldHelper foolsGoldHelper = ServiceLocator.getService(FoolsGoldHelper.class);
	
	//constructor
	public FoolsGoldEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		foolsGoldHelper.removePlayer(uuid, player);
	}
}
