package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import me.egg82.tcpp.services.LagRegistry;
import me.egg82.tcpp.services.LagTimeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<PlayerTeleportEvent> {
	//vars
	private IRegistry lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagTimeRegistry = ServiceLocator.getService(LagTimeRegistry.class);
	
	//constructor
	public LagEventCommand(PlayerTeleportEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
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
			Location from = event.getFrom();
			lagTimeRegistry.setRegister(uuid, Location.class, from);
			
			// Just teleporting the player to their old location after 0.5-1 seconds
			TaskUtil.runSync(new Runnable() {
				public void run() {
					player.teleport(from);
					
					// Wait 1.75-2.5 seconds until we start lagging movement again. More realistic this way
					TaskUtil.runSync(new Runnable() {
						public void run() {
							lagTimeRegistry.setRegister(uuid, Location.class, null);
						}
					}, MathUtil.fairRoundedRandom(35, 50));
				}
			}, MathUtil.fairRoundedRandom(10, 20));
		}
	}
}
