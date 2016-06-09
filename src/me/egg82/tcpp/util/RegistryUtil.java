package me.egg82.tcpp.util;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class RegistryUtil {
	//vars
	private static IRegistry bombRegistry = null;
	private static IRegistry electrifyRegistry = null;
	private static IRegistry burnRegistry = null;
	private static IRegistry starveRegistry = null;
	private static IRegistry hurtRegistry = null;
	private static IRegistry delayKillRegistry = null;
	private static IRegistry controlRegistry = null;
	private static IRegistry controllerRegistry = null;
	private static IRegistry vegetableRegistry = null;
	private static IRegistry vegetableInternRegistry = null;
	private static IRegistry infinityRegistry = null;
	private static IRegistry lavaBreakRegistry = null;
	private static IRegistry aloneRegistry = null;
	private static IRegistry annoyRegistry = null;
	private static IRegistry spartaRegistry = null;
	private static IRegistry rewindRegistry = null;
	private static IRegistry lagRegistry = null;
	
	//constructor
	public RegistryUtil() {
		
	}
	
	//public
	public static void intialize() {
		bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
		electrifyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ELECTRIFY_REGISTRY);
		burnRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BURN_REGISTRY);
		starveRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
		hurtRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.HURT_REGISTRY);
		delayKillRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.DELAY_KILL_REGISTRY);
		controlRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
		controllerRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
		vegetableRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
		vegetableInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_INTERN_REGISTRY);
		infinityRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.INFINITY_REGISTRY);
		lavaBreakRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAVA_BREAK_REGISTRY);
		aloneRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ALONE_REGISTRY);
		annoyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ANNOY_REGISTRY);
		spartaRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SPARTA_REGISTRY);
		rewindRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.REWIND_REGISTRY);
		lagRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_REGISTRY);
	}
	
	@SuppressWarnings("unchecked")
	public static void onQuit(String name, Player player) {
		bombRegistry.setRegister(name, null);
		electrifyRegistry.setRegister(name, null);
		burnRegistry.setRegister(name, null);
		starveRegistry.setRegister(name, null);
		hurtRegistry.setRegister(name, null);
		controlRegistry.setRegister(name, null);
		delayKillRegistry.setRegister(name, null);
		vegetableRegistry.setRegister(name, null);
		infinityRegistry.setRegister(name, null);
		lavaBreakRegistry.setRegister(name, null);
		aloneRegistry.setRegister(name, null);
		annoyRegistry.setRegister(name, null);
		spartaRegistry.setRegister(name, null);
		rewindRegistry.setRegister(name, null);
		lagRegistry.setRegister(name, null);
		
		if (controllerRegistry.contains(name)) {
			Player p = (Player) controllerRegistry.getRegister(name);
			if (p != null) {
				p.kickPlayer("You were being controlled, and your controller was kicked.");
			}
			controllerRegistry.setRegister(name, null);
		}
		
		if (vegetableInternRegistry.contains(name)) {
			HashMap<String, Object> map = (HashMap<String, Object>) vegetableInternRegistry.getRegister(name);
			Item potato = (Item) map.get("item");
			
			player.setGameMode((GameMode) map.get("mode"));
			
			vegetableInternRegistry.setRegister(name, null);
			
			potato.remove();
		}
	}
	public static void onDeath(String name, Player player) {
		bombRegistry.setRegister(name, null);
		electrifyRegistry.setRegister(name, null);
		burnRegistry.setRegister(name, null);
		starveRegistry.setRegister(name, null);
		hurtRegistry.setRegister(name, null);
		delayKillRegistry.setRegister(name, null);
		infinityRegistry.setRegister(name, null);
		spartaRegistry.setRegister(name, null);
	}
	
	//private
	
}
