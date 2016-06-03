package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerKickEvent;

import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.commands.EventCommand;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class PlayerKickEventCommand extends EventCommand {
	//vars
	IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	IRegistry electrifyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ELECTRIFY_REGISTRY);
	
	//constructor
	public PlayerKickEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerKickEvent e = (PlayerKickEvent) event;
		bombRegistry.setRegister(e.getPlayer().getName(), null);
		electrifyRegistry.setRegister(e.getPlayer().getName(), null);
	}
}
