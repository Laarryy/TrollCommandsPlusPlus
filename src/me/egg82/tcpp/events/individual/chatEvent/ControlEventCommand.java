package me.egg82.tcpp.events.individual.chatEvent;

import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class ControlEventCommand extends EventCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
	
	//constructor
	public ControlEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (controlRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			if (!permissionsManager.hasPermission(PermissionsType.CHAT_WHILE_CONTROLLED)) {
				e.setCancelled(true);
			}
		}
	}
}
