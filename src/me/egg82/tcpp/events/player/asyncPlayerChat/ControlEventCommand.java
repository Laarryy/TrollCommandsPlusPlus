package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlEventCommand extends EventCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	
	//constructor
	public ControlEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		Player controlledPlayer = (Player) controlRegistry.getRegister(player.getUniqueId().toString());
		
		if (controlledPlayer != null) {
			// Player is controlling someone
			controlledPlayer.chat(e.getMessage());
			e.setCancelled(true);
		}
		
		String controllerUuid = controlRegistry.getName(player);
		if (controllerUuid != null) {
			// Player is being controlled by someone
			if (!CommandUtil.hasPermission(player, PermissionsType.CHAT_WHILE_CONTROLLED)) {
				player.sendMessage(MessageType.NO_CHAT);
				e.setCancelled(true);
			}
		}
	}
}
