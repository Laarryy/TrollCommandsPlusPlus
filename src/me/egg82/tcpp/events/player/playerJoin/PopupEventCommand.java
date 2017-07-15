package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.PopupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class PopupEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry popupRegistry = ServiceLocator.getService(PopupRegistry.class);
	
	//constructor
	public PopupEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (popupRegistry.hasRegister(uuid)) {
			popupRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
