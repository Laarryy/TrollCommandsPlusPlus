package me.egg82.tcpp.events.block.blockBreak;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.events.custom.LagBlockBreakEvent;
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
	protected void onExecute(long elapsedMilliseconds) {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (!lagRegistry.hasRegister(e.getPlayer().getUniqueId().toString())) {
			return;
		}
		
		// Make sure we're not "lagging" our own lag event. Infinite loops, ahoy!
		if (ReflectUtil.doesExtend(event.getClass(), LagBlockBreakEvent.class)) {
			return;
		}
		
		e.setCancelled(true);
		
		// Just "re-sending" the NEW event after a random interval
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
			public void run() {
				// Events are "snapshots" - no need to save required elements
				Bukkit.getServer().getPluginManager().callEvent(new LagBlockBreakEvent(e.getBlock(), e.getPlayer()));
			}
		}, MathUtil.fairRoundedRandom(40, 60));
	}
}
