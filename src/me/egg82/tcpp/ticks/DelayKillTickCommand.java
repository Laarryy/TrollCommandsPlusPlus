package me.egg82.tcpp.ticks;

import java.util.EnumMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.services.DelayKillRegistry;
import me.egg82.tcpp.services.DelayKillTicksRegistry;
import me.egg82.tcpp.services.DelayKillTimeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class DelayKillTickCommand extends TickCommand {
	//vars
	private IRegistry delayKillRegistry = (IRegistry) ServiceLocator.getService(DelayKillRegistry.class);
	private IRegistry delayKillTimeRegistry = (IRegistry) ServiceLocator.getService(DelayKillTimeRegistry.class);
	private IRegistry delayKillTicksRegistry = (IRegistry) ServiceLocator.getService(DelayKillTicksRegistry.class);
	
	//constructor
	public DelayKillTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = delayKillRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) delayKillRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		long timePassed = System.currentTimeMillis() - (long) delayKillTimeRegistry.getRegister(uuid);
		long delay = (long) delayKillTicksRegistry.getRegister(uuid);
		
		if (timePassed < delay * 1000) {
			return;
		}
		
		player.setHealth(0.0d);
		EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, Double.MAX_VALUE)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(Double.MAX_VALUE))));
		Bukkit.getPluginManager().callEvent(damageEvent);
		damageEvent.getEntity().setLastDamageCause(damageEvent);
	}
}
