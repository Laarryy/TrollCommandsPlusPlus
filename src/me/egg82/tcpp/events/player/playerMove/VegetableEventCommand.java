package me.egg82.tcpp.events.player.playerMove;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.registries.VegetableLocationRegistry;
import me.egg82.tcpp.services.registries.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class VegetableEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	private IRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
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
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
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
