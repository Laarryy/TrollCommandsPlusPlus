package me.egg82.tcpp.events.individual.playerChat;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.StringUtil;

public class GarbleEventCommand extends EventCommand {
	//vars
	private IRegistry garbleRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.GARBLE_REGISTRY);
	
	//constructor
	public GarbleEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		if (garbleRegistry.contains(e.getPlayer().getUniqueId().toString())) {
			e.setMessage(StringUtil.randomString(e.getMessage().length()));
		}
	}
}
