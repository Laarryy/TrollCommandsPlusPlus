package me.egg82.tcpp.events.player.playerMove;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.LagRegistry;
import me.egg82.tcpp.services.LagTimeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagTimeRegistry = (IRegistry) ServiceLocator.getService(LagTimeRegistry.class);
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(InitRegistry.class);
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!lagRegistry.hasRegister(uuid)) {
			return;
		}
		
		// We're already lagging the player's movement. No need to do it twice.
		if (lagTimeRegistry.hasRegister(uuid)) {
			return;
		}
		
		// 15% chance that we lag the player
		if (Math.random() <= 0.15d) {
			Location from = e.getFrom();
			lagTimeRegistry.setRegister(uuid, Location.class, from);
			
			// Just teleporting the player to their old location after 0.5-1 seconds
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
				public void run() {
					player.teleport(e.getFrom());
					
					// Wait 1.75-2.5 seconds until we start lagging movement again. More realistic this way
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
						public void run() {
							lagTimeRegistry.setRegister(uuid, Location.class, null);
						}
					}, MathUtil.fairRoundedRandom(35, 50));
				}
			}, MathUtil.fairRoundedRandom(10, 20));
		}
	}
}