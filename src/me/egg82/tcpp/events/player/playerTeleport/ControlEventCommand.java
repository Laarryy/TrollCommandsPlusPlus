package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class ControlEventCommand extends EventCommand<PlayerTeleportEvent> {
	//vars
	private IRegistry controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
	//constructor
	public ControlEventCommand(PlayerTeleportEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Player controlledPlayer = controlRegistry.getRegister(player.getUniqueId().toString(), Player.class);
		
		if (controlledPlayer != null) {
			// Player is controlling someone
			if (!CommandUtil.hasPermission(controlledPlayer, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
				controlledPlayer.teleport(LocationUtil.makeEqualXYZ(LocationUtil.getLocationBehind(player.getLocation(), 1.5d).subtract(0.0d, 1.0d, 0.0d), controlledPlayer.getLocation()));
			}
		}
		
		String controllerUuid = controlRegistry.getName(player);
		Player controller = CommandUtil.getPlayerByUuid(controllerUuid);
		
		if (controller != null) {
			// Player is being controlled by someone
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
				event.setTo(LocationUtil.makeEqualXYZ(LocationUtil.getLocationBehind(controller.getLocation(), 1.5d).subtract(0.0d, 1.0d, 0.0d), event.getTo()));
			}
		}
	}
}
