package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private JavaPlugin plugin = (JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin");
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	@SuppressWarnings("deprecation")
	protected void onExecute(long elapsedMilliseconds) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		// Snapshot of the event, since this event could be modified or destroyed later
		Set<Player> recipients = e.getRecipients();
		String format = e.getFormat();
		String playerName = player.getDisplayName();
		String message = e.getMessage();
		
		Runnable chatRunner = new Runnable() {
			public void run() {
				recipients.forEach((v) -> {
					v.sendMessage(String.format(format, playerName, message));
				});
			}
		};
		
		e.setCancelled(true);
		
		// Manually chat for the player after a 2-3 second delay
		if (e.isAsynchronous()) {
			Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, chatRunner, MathUtil.fairRoundedRandom(40, 60));
		} else {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, chatRunner, MathUtil.fairRoundedRandom(40, 60));
		}
	}
}
