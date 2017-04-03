package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.AmnesiaMessageRegistry;
import me.egg82.tcpp.services.AmnesiaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AmnesiaEventCommand extends EventCommand {
	//vars
	private IRegistry amnesiaRegistry = (IRegistry) ServiceLocator.getService(AmnesiaRegistry.class);
	private IRegistry amnesiaMessageRegistry = (IRegistry) ServiceLocator.getService(AmnesiaMessageRegistry.class);
	
	//constructor
	public AmnesiaEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		String playerName = e.getPlayer().getDisplayName();
		Set<Player> recipients = e.getRecipients();
		Iterator<Player> i = recipients.iterator();
		
		while (i.hasNext()) {
			Player v = i.next();
			String uuid = v.getUniqueId().toString();
			
			if (amnesiaRegistry.hasRegister(uuid)) {
				List<String> messages = (List<String>) amnesiaMessageRegistry.getRegister(uuid);
				
				// Don't try to optimize RNG. Think about it for a sec.
				
				if (Math.random() <= 0.05d) {
					//remove
					i.remove();
				} else {
					if (Math.random() <= 0.2d) {
						//delay
						messages.add(String.format(e.getFormat(), playerName, e.getMessage()));
						i.remove();
					}
					if (Math.random() <= 0.1d) {
						//repeat
						messages.add(String.format(e.getFormat(), playerName, e.getMessage()));
					}
				}
			}
		}
	}
}
