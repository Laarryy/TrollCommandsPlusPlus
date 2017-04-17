package me.egg82.tcpp.events.entity.entityDamage;

import java.util.EnumMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(InitRegistry.class);
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		if (e.getEntityType() != EntityType.PLAYER) {
			return;
		}
		
		Player player = (Player) e.getEntity();
		
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		e.setCancelled(true);
		
		// Manually doing the event after a random interval
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
			public void run() {
				// Cause the player damage
				player.damage(e.getDamage());
				EntityDamageEvent damageEvent = new EntityDamageEvent(player, e.getCause(), new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, e.getDamage())), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(e.getDamage()))));
				Bukkit.getPluginManager().callEvent(damageEvent);
				damageEvent.getEntity().setLastDamageCause(damageEvent);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
