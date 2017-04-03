package me.egg82.tcpp.events.player.playerMove;

import java.util.EnumMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerMoveEvent;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.services.BrittleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BrittleEventCommand extends EventCommand {
	//vars
	private IRegistry brittleRegistry = (IRegistry) ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (brittleRegistry.hasRegister(player.getUniqueId().toString())) {
			Location to = e.getTo();
			double toY = to.getY();
			double blockY = (double) to.getBlockY();
			
			if (toY == blockY && toY < e.getFrom().getY()) {
				player.setHealth(0.0d);
				EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.FALL, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, Double.MAX_VALUE)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(Double.MAX_VALUE))));
				Bukkit.getPluginManager().callEvent(damageEvent);
				damageEvent.getEntity().setLastDamageCause(damageEvent);
			}
		}
	}
}
