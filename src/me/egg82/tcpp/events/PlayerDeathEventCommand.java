package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.commands.EventCommand;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class PlayerDeathEventCommand extends EventCommand {
	//vars
	IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	
	//constructor
	public PlayerDeathEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		bombRegistry.setRegister(e.getEntity().getName(), null);
	}
}