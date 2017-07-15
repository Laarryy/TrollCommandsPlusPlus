package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
	//constructor
	public ControlEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Player controlledPlayer = controlRegistry.getRegister(player.getUniqueId().toString(), Player.class);
		
		if (controlledPlayer != null) {
			// Player is controlling someone
			controlledPlayer.chat(event.getMessage());
			event.setCancelled(true);
		}
		
		String controllerUuid = controlRegistry.getName(player);
		if (controllerUuid != null) {
			// Player is being controlled by someone
			if (!CommandUtil.hasPermission(player, PermissionsType.CHAT_WHILE_CONTROLLED)) {
				player.sendMessage(MessageType.NO_CHAT_CONTROL);
				event.setCancelled(true);
			}
		}
	}
}
