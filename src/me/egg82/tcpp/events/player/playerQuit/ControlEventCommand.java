package me.egg82.tcpp.events.player.playerQuit;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.ControlRegistry;
import me.egg82.tcpp.util.ControlHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlEventCommand extends EventCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	
	private ControlHelper controlHelper = (ControlHelper) ServiceLocator.getService(ControlHelper.class);
	
	//constructor
	public ControlEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerQuitEvent e = (PlayerQuitEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (controlRegistry.hasRegister(uuid)) {
			// The player that quit/got kicked was controlling someone.
			player.sendMessage("Your controller has quit or been kicked!");
			controlHelper.uncontrol(uuid, player);
		}
		
		String controllerUuid = controlRegistry.getName(player);
		
		if (controllerUuid != null) {
			// The player that quit/got kicked was being controlled by someone.
			Player controller = CommandUtil.getPlayerByUuid(controllerUuid);
			controller.sendMessage(player.getName() + " has quit or been kicked!");
			controlHelper.uncontrol(controllerUuid, controller);
		}
	}
}
