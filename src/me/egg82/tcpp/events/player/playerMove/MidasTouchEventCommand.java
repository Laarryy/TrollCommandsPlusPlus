package me.egg82.tcpp.events.player.playerMove;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.registries.MidasTouchRegistry;
import me.egg82.tcpp.util.DisplayHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class MidasTouchEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IRegistry<UUID> midasTouchRegistry = ServiceLocator.getService(MidasTouchRegistry.class);
	
	private DisplayHelper displayHelper = ServiceLocator.getService(DisplayHelper.class);
	
	//constructor
	public MidasTouchEventCommand(PlayerMoveEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Material type = midasTouchRegistry.getRegister(player.getUniqueId(), Material.class);
		
		if (type == null) {
			return;
		}
		
		Location playerLoc = player.getLocation();
		Location playerEyeLoc = player.getEyeLocation();
		
		Set<Location> blocks = displayHelper.getBlockLocationsAround(playerLoc);
		for (Location l : blocks) {
			if (!l.getBlock().getType().isSolid()) {
				continue;
			}
			if (l.getBlock().getType() == type) {
				continue;
			}
			
			l.add(0.5d, 0.0d, 0.5d);
			if (playerLoc.distanceSquared(l) <= 1.5625 || playerEyeLoc.distanceSquared(l) <= 1.5625) { //1.25
				l.getBlock().setType(type);
			}
		}
	}
}
