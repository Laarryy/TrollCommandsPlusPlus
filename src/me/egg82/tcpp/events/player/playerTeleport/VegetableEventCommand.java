package me.egg82.tcpp.events.player.playerTeleport;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.VegetableLocationRegistry;
import me.egg82.tcpp.registries.VegetableRegistry;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class VegetableEventCommand extends EventHandler<PlayerTeleportEvent> {
	//vars
	private IVariableRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IVariableRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableEventCommand() {
		super();
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
			if (!player.hasPermission(PermissionsType.FREECAM_WHILE_VEGETABLE)) {
				if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
					event.setCancelled(true);
					player.teleport(vegetableLocationRegistry.getRegister(uuid, Location.class).clone().add(0.0d, -1.0d, 0.0d));
				} else {
					event.setTo(LocationUtil.makeEqualXYZ(vegetableLocationRegistry.getRegister(uuid, Location.class).clone().add(0.0d, -1.0d, 0.0d), event.getTo()));
				}
			}
		}
	}
}
