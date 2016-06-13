package me.egg82.tcpp.ticks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.plugin.enums.CustomServiceType;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;

public class DelayKillTickCommand extends Command {
	//vars
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(CustomServiceType.TICK_HANDLER);
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.DELAY_KILL_REGISTRY);
	
	//constructor
	public DelayKillTickCommand() {
		
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		String[] names = reg.registryNames();
		for (String name : names) {
			e(name, (HashMap<String, Object>) reg.getRegister(name));
		}
	}
	@SuppressWarnings("deprecation")
	private void e(String name, HashMap<String, Object> m) {
		long timePassed = System.currentTimeMillis() - (long) m.get("time");
		int delay = (int) m.get("delay");
		
		if (timePassed < delay * 1000) {
			tickHandler.addDelayedTickCommand(name, DelayKillTickCommand.class, 20);
			return;
		}
		
		Player player = (Player) m.get("player");
		
		if (player == null) {
			return;
		}
		
		EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000.0d);
		Bukkit.getPluginManager().callEvent(damageEvent);
		damageEvent.getEntity().setLastDamageCause(damageEvent);
		player.setHealth(0.0d);
	}
}
