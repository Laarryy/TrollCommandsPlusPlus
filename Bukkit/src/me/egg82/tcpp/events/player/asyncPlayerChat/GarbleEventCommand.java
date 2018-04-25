package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.registries.GarbleRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;
import ninja.egg82.utils.StringUtil;

public class GarbleEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IVariableRegistry<UUID> garbleRegistry = ServiceLocator.getService(GarbleRegistry.class);
	
	//constructor
	public GarbleEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (garbleRegistry.hasRegister(player.getUniqueId())) {
			String oldMessage = String.format(event.getFormat(), player.getDisplayName(), event.getMessage());
			event.setMessage(StringUtil.shuffle(event.getMessage()));
			event.getRecipients().remove(player);
			player.sendMessage(oldMessage);
		}
	}
}
