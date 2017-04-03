package me.egg82.tcpp.events.player.playerQuit;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.ControlInventoryRegistry;
import me.egg82.tcpp.services.ControlModeRegistry;
import me.egg82.tcpp.services.ControlRegistry;
import me.egg82.tcpp.util.IDisguiseHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class ControlEventCommand extends EventCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(ControlRegistry.class);
	private IRegistry controlModeRegistry = (IRegistry) ServiceLocator.getService(ControlModeRegistry.class);
	private IRegistry controlInventoryRegistry = (IRegistry) ServiceLocator.getService(ControlInventoryRegistry.class);
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(IDisguiseHelper.class);
	
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
		
		Player controlledPlayer = (Player) controlRegistry.getRegister(uuid);
		
		if (controlledPlayer != null) {
			// The player that quit/got kicked was controlling someone.
			// TODO finish this
		}
		
		Player controllingPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getName(player));
		
		if (controllingPlayer != null) {
			// The player that quit/got kicked was being controlled by someone.
			// TODO finish this
		}
	}
}
