package me.egg82.tcpp.events.player.playerInteract;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class VegetableEventCommand extends EventCommand<PlayerInteractEvent> {
	//vars
	private IRegistry vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	
	//constructor
	public VegetableEventCommand(PlayerInteractEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (vegetableRegistry.hasRegister(event.getPlayer().getUniqueId().toString())) {
			if (!CommandUtil.hasPermission(player, PermissionsType.FREECAM_WHILE_VEGETABLE)) {
				event.setCancelled(true);
			}
		}
	}
}
