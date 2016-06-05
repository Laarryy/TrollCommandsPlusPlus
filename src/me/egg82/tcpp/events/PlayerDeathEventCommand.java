package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class PlayerDeathEventCommand extends EventCommand {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	private IRegistry electrifyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ELECTRIFY_REGISTRY);
	private IRegistry burnRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BURN_REGISTRY);
	private IRegistry starveRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
	private IRegistry hurtRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.HURT_REGISTRY);
	
	//constructor
	public PlayerDeathEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		String name = ((PlayerDeathEvent) event).getEntity().getName();
		bombRegistry.setRegister(name, null);
		electrifyRegistry.setRegister(name, null);
		burnRegistry.setRegister(name, null);
		starveRegistry.setRegister(name, null);
		hurtRegistry.setRegister(name, null);
	}
}