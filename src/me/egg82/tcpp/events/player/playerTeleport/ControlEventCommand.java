package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class ControlEventCommand extends EventCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	
	//constructor
	public ControlEventCommand(Event event) {
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
		Player controlledPlayer = (Player) controlRegistry.getRegister(player.getUniqueId().toString());
		
		if (controlledPlayer != null) {
			// Player is controlling someone
			if (!CommandUtil.hasPermission(controlledPlayer, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
				controlledPlayer.teleport(LocationUtil.makeEqualXYZ(player.getLocation(), controlledPlayer.getLocation()));
			}
		}
		
		String controllerUuid = controlRegistry.getName(player);
		if (controllerUuid != null) {
			// Player is being controlled by someone
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
				e.setTo(LocationUtil.makeEqualXYZ(e.getFrom(), e.getTo()));
			}
		}
	}
}
