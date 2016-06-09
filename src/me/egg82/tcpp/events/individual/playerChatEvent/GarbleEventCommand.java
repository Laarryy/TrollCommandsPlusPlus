package me.egg82.tcpp.events.individual.playerChatEvent;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class GarbleEventCommand extends EventCommand {
	//vars
	private IRegistry garbleRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.GARBLE_REGISTRY);
	private static final char [] subset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}|:\"<>?`-=[]\\;',./".toCharArray();
	
	//constructor
	public GarbleEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		if (garbleRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setMessage(randString(e.getMessage().length()));
		}
	}
	private String randString(int length) {
		char buffer[] = new char[length];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = subset[MathUtil.fairRoundedRandom(0, subset.length - 1)];
		}
		return new String(buffer);
	}
}
