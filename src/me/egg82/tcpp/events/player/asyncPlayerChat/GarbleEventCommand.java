package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.GarbleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.StringUtil;

public class GarbleEventCommand extends EventCommand {
	//vars
	IRegistry garbleRegistry = (IRegistry) ServiceLocator.getService(GarbleRegistry.class);
	
	//constructor
	public GarbleEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (garbleRegistry.hasRegister(e.getPlayer().getUniqueId().toString())) {
			e.setMessage(StringUtil.randomString(e.getMessage().length()));
		}
	}
}
