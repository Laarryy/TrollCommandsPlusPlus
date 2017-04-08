package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.events.custom.LagAsyncPlayerChatEvent;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(InitRegistry.class);
	
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
		
		// Make sure we're not "lagging" our own lag event. Infinite loops, ahoy!
		if (ReflectUtil.doesExtend(event.getClass(), LagAsyncPlayerChatEvent.class)) {
			return;
		}
		
		e.setCancelled(true);
		
		boolean asynchronous = e.isAsynchronous();
		
		Runnable chatRunner = new Runnable() {
			public void run() {
				// Events are "snapshots" - no need to save required elements
				Bukkit.getServer().getPluginManager().callEvent(new LagAsyncPlayerChatEvent(asynchronous, player, e.getMessage(), e.getRecipients()));
			}
		};
		
		// Just "re-sending" the NEW event after a random interval
		if (asynchronous) {
			Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), chatRunner, MathUtil.fairRoundedRandom(40, 60));
		} else {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), chatRunner, MathUtil.fairRoundedRandom(40, 60));
		}
	}
}
