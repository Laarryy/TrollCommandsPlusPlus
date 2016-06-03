package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.commands.EventCommand;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class PlayerMoveEventCommand extends EventCommand {
	//vars
	IRegistry freezeRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.FREEZE_REGISTRY);
	
	//constructor
	public PlayerMoveEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		if (freezeRegistry.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}
}
