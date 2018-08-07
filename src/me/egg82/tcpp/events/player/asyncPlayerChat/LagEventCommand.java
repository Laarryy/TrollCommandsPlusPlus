package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.registries.LagRegistry;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventHandler<AsyncPlayerChatEvent> {
	//vars
	private IVariableRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
	
	//constructor
	public LagEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (!lagRegistry.hasRegister(player.getUniqueId())) {
			return;
		}
		
		// Snapshot of the event, since this event could be modified or destroyed later
		Set<Player> recipients = event.getRecipients();
		String format = event.getFormat();
		String playerName = player.getDisplayName();
		String message = event.getMessage();
		
		Runnable chatRunner = new Runnable() {
			public void run() {
				recipients.forEach((v) -> {
					v.sendMessage(String.format(format, playerName, message));
				});
			}
		};
		
		event.setCancelled(true);
		
		// Manually chat for the player after a 2-3 second delay
		if (event.isAsynchronous()) {
			TaskUtil.runAsync(chatRunner, MathUtil.fairRoundedRandom(40, 60));
		} else {
			TaskUtil.runSync(chatRunner, MathUtil.fairRoundedRandom(40, 60));
		}
	}
}
