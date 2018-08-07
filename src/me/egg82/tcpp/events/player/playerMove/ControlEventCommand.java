package me.egg82.tcpp.events.player.playerMove;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.ControlRegistry;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ControlEventCommand extends EventHandler<PlayerMoveEvent> {
	//vars
	private IVariableRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
	//constructor
	public ControlEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID playerUuid = player.getUniqueId();
		
		if (controlRegistry.hasRegister(playerUuid)) {
			// Player is controlling someone
			Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(playerUuid, UUID.class));
			
			if (controlledPlayer != null) {
				if (!controlledPlayer.hasPermission(PermissionsType.FREECAM_WHILE_CONTROLLED)) {
					controlledPlayer.teleport(LocationUtil.makeEqualXYZ(LocationUtil.getLocationBehind(player.getLocation(), 1.5d, false).subtract(0.0d, 1.0d, 0.0d), controlledPlayer.getLocation()));
				}
			}
		}
		
		UUID controllerUuid = controlRegistry.getKey(player.getUniqueId());
		
		if (controllerUuid != null) {
			// Player is being controlled by someone
			Player controller = CommandUtil.getPlayerByUuid(controllerUuid);
			
			if (controller != null) {
				if (!player.hasPermission(PermissionsType.FREECAM_WHILE_CONTROLLED)) {
					event.setTo(LocationUtil.makeEqualXYZ(LocationUtil.getLocationBehind(controller.getLocation(), 1.5d, false).subtract(0.0d, 1.0d, 0.0d), event.getTo()));
				}
			}
		}
	}
}
