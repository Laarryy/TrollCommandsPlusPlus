package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class ControlEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
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
		UUID playerUuid = player.getUniqueId();
		
		if (controlRegistry.hasRegister(playerUuid)) {
			// Player is controlling someone
			Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(playerUuid, UUID.class));
			
			if (controlledPlayer != null) {
				controlledPlayer.chat(event.getMessage());
				event.setCancelled(true);
			}
		}
		
		UUID controllerUuid = controlRegistry.getKey(playerUuid);
		if (controllerUuid != null) {
			// Player is being controlled by someone
			if (!CommandUtil.hasPermission(player, PermissionsType.CHAT_WHILE_CONTROLLED)) {
				player.sendMessage(LanguageUtil.getString(LanguageType.NO_CHAT_CONTROL));
				event.setCancelled(true);
			}
		}
	}
}
