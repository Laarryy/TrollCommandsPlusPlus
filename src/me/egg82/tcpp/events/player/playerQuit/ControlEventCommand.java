package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.ControlRegistry;
import me.egg82.tcpp.util.ControlHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);
	
	private ControlHelper controlHelper = ServiceLocator.getService(ControlHelper.class);
	
	//constructor
	public ControlEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (controlRegistry.hasRegister(uuid)) {
			// The player that quit/got kicked was controlling someone.
			player.sendMessage("Your controller has quit or been kicked!");
			controlHelper.uncontrol(uuid, player);
		}
		
		UUID controllerUuid = controlRegistry.getKey(player.getUniqueId());
		
		if (controllerUuid != null) {
			// The player that quit/got kicked was being controlled by someone.
			Player controller = CommandUtil.getPlayerByUuid(controllerUuid);
			if (controller != null) {
				controller.sendMessage(player.getName() + " has quit or been kicked!");
				controlHelper.uncontrol(controllerUuid, controller);
			}
		}
	}
}
