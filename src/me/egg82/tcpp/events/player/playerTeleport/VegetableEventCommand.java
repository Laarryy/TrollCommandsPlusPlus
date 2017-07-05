package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableLocationRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry vegetableLocationRegistry = (IRegistry) ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerTeleportEvent e = (PlayerTeleportEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (vegetableRegistry.hasRegister(uuid)) {
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
				e.setTo(LocationUtil.makeEqualXYZ(((Location) vegetableLocationRegistry.getRegister(uuid)).clone().add(0.0d, -1.0d, 0.0d), e.getTo()));
			}
		}
	}
}
