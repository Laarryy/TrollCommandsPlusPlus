package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class FreezeEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (freezeRegistry.hasRegister(player.getUniqueId().toString()) && !CommandUtil.hasPermission(player, PermissionsType.CHAT_WHILE_FROZEN)) {
			event.setCancelled(true);
			player.sendMessage(MessageType.NO_CHAT_FROZEN);
		}
	}
}
