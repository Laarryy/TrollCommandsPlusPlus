package me.egg82.tcpp.events.player.playerTeleport;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableLocationRegistry;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class VegetableEventCommand extends EventCommand<PlayerTeleportEvent> {
	//vars
	private IRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableEventCommand(PlayerTeleportEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (vegetableRegistry.hasRegister(uuid)) {
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
				event.setTo(LocationUtil.makeEqualXYZ((vegetableLocationRegistry.getRegister(uuid, Location.class)).clone().add(0.0d, -1.0d, 0.0d), event.getTo()));
			}
		}
	}
}
