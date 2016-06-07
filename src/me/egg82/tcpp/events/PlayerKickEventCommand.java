package me.egg82.tcpp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerKickEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class PlayerKickEventCommand extends EventCommand {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	private IRegistry electrifyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ELECTRIFY_REGISTRY);
	private IRegistry burnRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BURN_REGISTRY);
	private IRegistry starveRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
	private IRegistry hurtRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.HURT_REGISTRY);
	private IRegistry delayKillRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.DELAY_KILL_REGISTRY);
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
	private IRegistry controllerRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
	
	//constructor
	public PlayerKickEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		String name = ((PlayerKickEvent) event).getPlayer().getName().toLowerCase();
		bombRegistry.setRegister(name, null);
		electrifyRegistry.setRegister(name, null);
		burnRegistry.setRegister(name, null);
		starveRegistry.setRegister(name, null);
		hurtRegistry.setRegister(name, null);
		controlRegistry.setRegister(name, null);
		delayKillRegistry.setRegister(name, null);
		
		if (controllerRegistry.contains(name)) {
			Player p = (Player) controllerRegistry.getRegister(name);
			if (p != null) {
				p.kickPlayer("You were being controlled, and your controller was kicked.");
			}
			controllerRegistry.setRegister(name, null);
		}
	}
}
