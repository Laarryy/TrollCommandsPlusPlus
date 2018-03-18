package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.registries.LagRegistry;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
	
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
				if (event.isAsynchronous()) {
					ServiceLocator.getService(IExceptionHandler.class).addThread(Thread.currentThread());
				}
				
				recipients.forEach((v) -> {
					v.sendMessage(String.format(format, playerName, message));
				});
				
				if (event.isAsynchronous()) {
					ServiceLocator.getService(IExceptionHandler.class).removeThread(Thread.currentThread());
				}
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
