package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.SlowUndoRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SlowUndoEventcommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry slowUndoRegistry = ServiceLocator.getService(SlowUndoRegistry.class);
	
	//constructor
	public SlowUndoEventcommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (slowUndoRegistry.hasRegister(uuid)) {
			slowUndoRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
