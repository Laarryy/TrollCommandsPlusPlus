package me.egg82.tcpp.events.player.playerInteractEntity;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.ControlRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlEventCommand extends EventCommand<PlayerInteractEntityEvent> {
	//vars
	private IRegistry controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
	//constructor
	public ControlEventCommand(PlayerInteractEntityEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		String controllerUuid = controlRegistry.getName(player);
		if (controllerUuid != null) {
			// Player is being controlled by someone
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
				event.setCancelled(true);
			}
		}
	}
}
