package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.GarbleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.StringUtil;

public class GarbleEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	IRegistry garbleRegistry = ServiceLocator.getService(GarbleRegistry.class);
	
	//constructor
	public GarbleEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (garbleRegistry.hasRegister(player.getUniqueId().toString())) {
			String oldMessage = String.format(event.getFormat(), player.getDisplayName(), event.getMessage());
			event.setMessage(StringUtil.randomString(event.getMessage().length()));
			event.getRecipients().remove(player);
			player.sendMessage(oldMessage);
		}
	}
}
