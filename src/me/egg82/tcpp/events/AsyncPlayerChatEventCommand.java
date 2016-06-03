package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.commands.EventCommand;
import com.egg82.registry.interfaces.IRegistry;
import com.egg82.utils.MathUtil;

import me.egg82.tcpp.enums.PluginServiceType;

public class AsyncPlayerChatEventCommand extends EventCommand {
	//vars
	IRegistry garbleRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.GARBLE_REGISTRY);
	private static final char [] subset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}|:\"<>?`-=[]\\;',./".toCharArray();
	
	//constructor
	public AsyncPlayerChatEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		if (garbleRegistry.contains(e.getPlayer().getName())) {
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
