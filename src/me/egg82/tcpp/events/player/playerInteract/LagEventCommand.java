package me.egg82.tcpp.events.player.playerInteract;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.events.custom.LagPlayerInteractEvent;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

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
	protected void onExecute(long elapsedMilliseconds) {
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		// Make sure we're not "lagging" our own lag event. Infinite loops, ahoy!
		if (event instanceof LagPlayerInteractEvent) {
			return;
		}
		
		e.setCancelled(true);
		
		// Just "re-sending" the NEW event after a random interval
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
			public void run() {
				// Events are "snapshots" - no need to save required elements
				Bukkit.getServer().getPluginManager().callEvent(new LagPlayerInteractEvent(player, e.getAction(), e.getItem(), e.getClickedBlock(), e.getBlockFace(), e.getHand()));
			}
		}, MathUtil.fairRoundedRandom(40, 60));
	}
}
