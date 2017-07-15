package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.AmnesiaMessageRegistry;
import me.egg82.tcpp.services.AmnesiaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AmnesiaEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry amnesiaRegistry = ServiceLocator.getService(AmnesiaRegistry.class);
	private IRegistry amnesiaMessageRegistry = ServiceLocator.getService(AmnesiaMessageRegistry.class);
	
	//constructor
	public AmnesiaEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		String playerName = event.getPlayer().getDisplayName();
		Set<Player> recipients = event.getRecipients();
		Iterator<Player> i = recipients.iterator();
		
		while (i.hasNext()) {
			Player v = i.next();
			String uuid = v.getUniqueId().toString();
			
			if (amnesiaRegistry.hasRegister(uuid)) {
				List<String> messages = amnesiaMessageRegistry.getRegister(uuid, List.class);
				
				// Don't try to optimize RNG. Think about it for a sec.
				
				if (Math.random() <= 0.05d) {
					//remove
					i.remove();
				} else {
					if (Math.random() <= 0.2d) {
						//delay
						messages.add(String.format(event.getFormat(), playerName, event.getMessage()));
						i.remove();
					}
					if (Math.random() <= 0.1d) {
						//repeat
						messages.add(String.format(event.getFormat(), playerName, event.getMessage()));
					}
				}
			}
		}
	}
}
