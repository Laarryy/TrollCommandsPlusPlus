package me.egg82.tcpp.events.individual.playerMoveEvent;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class FreezeEventCommand extends EventCommand {
	//vars
	private IRegistry freezeRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.FREEZE_REGISTRY);
	
	//constructor
	public FreezeEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		if (freezeRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setCancelled(true);
		}
	}
}
