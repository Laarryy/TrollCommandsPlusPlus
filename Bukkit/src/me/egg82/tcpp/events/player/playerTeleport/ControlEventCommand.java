package me.egg82.tcpp.events.player.playerTeleport;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.registries.ControlRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class ControlEventCommand extends EventCommand<PlayerTeleportEvent> {
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
				if (!CommandUtil.hasPermission(controlledPlayer, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
					controlledPlayer.teleport(LocationUtil.makeEqualXYZ(LocationUtil.getLocationBehind(player.getLocation(), 1.5d).subtract(0.0d, 1.0d, 0.0d), controlledPlayer.getLocation()));
				}
			}
		}
		
		UUID controllerUuid = controlRegistry.getKey(player.getUniqueId());
		
		if (controllerUuid != null) {
			// Player is being controlled by someone
			Player controller = CommandUtil.getPlayerByUuid(controllerUuid);
			
			if (controller != null) {
				if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
					event.setTo(LocationUtil.makeEqualXYZ(LocationUtil.getLocationBehind(controller.getLocation(), 1.5d).subtract(0.0d, 1.0d, 0.0d), event.getTo()));
				}
			}
		}
	}
}
