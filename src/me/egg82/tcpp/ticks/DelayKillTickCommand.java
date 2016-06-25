package me.egg82.tcpp.ticks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;

public class DelayKillTickCommand extends TickCommand {
	//vars
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(SpigotServiceType.TICK_HANDLER);
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.DELAY_KILL_REGISTRY);
	
	//constructor
	public DelayKillTickCommand() {
		super();
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
		
		player.setHealth(0.0d);
		EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, Double.MAX_VALUE);
		Bukkit.getPluginManager().callEvent(damageEvent);
		damageEvent.getEntity().setLastDamageCause(damageEvent);
	}
}
