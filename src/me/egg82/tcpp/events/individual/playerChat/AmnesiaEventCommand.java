package me.egg82.tcpp.events.individual.playerChat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class AmnesiaEventCommand extends EventCommand {
	//vars
	private IRegistry amnesiaRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_REGISTRY);
	private IRegistry amnesiaInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_INTERN_REGISTRY);
	
	//constructor
	public AmnesiaEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		Set<Player> rec = e.getRecipients();
		Iterator<Player> i = rec.iterator();
		ArrayList<Player> rem = new ArrayList<Player>();
		
		while (i.hasNext()) {
			Player p = i.next();
			String uuid = p.getUniqueId().toString();
			
			if (amnesiaRegistry.contains(uuid)) {
				ArrayList<ImmutableMap<String, Object>> maps = (ArrayList<ImmutableMap<String, Object>>) amnesiaInternRegistry.getRegister(uuid);
				
				//remove
				if (Math.random() <= 0.05) {
					rem.add(p);
				} else {
					//delay
					if (Math.random() <= 0.2) {
						maps.add(ImmutableMap.of("player", e.getPlayer().getDisplayName(), "message", e.getMessage(), "format", e.getFormat()));
						rem.add(p);
					}
					//repeat
					if (Math.random() <= 0.1) {
						maps.add(ImmutableMap.of("player", e.getPlayer().getDisplayName(), "message", e.getMessage(), "format", e.getFormat()));
					}
				}
			}
		}
		
		e.getRecipients().removeAll(rem);
	}
}
