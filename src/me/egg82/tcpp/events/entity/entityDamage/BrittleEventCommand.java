package me.egg82.tcpp.events.entity.entityDamage;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.services.BrittleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityUtil;

public class BrittleEventCommand extends EventCommand {
	//vars
	private IEntityUtil entityUtil = (IEntityUtil) ServiceLocator.getService(IEntityUtil.class);
	
	private IRegistry brittleRegistry = (IRegistry) ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand(Event event) {
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
		
		if (brittleRegistry.hasRegister(player.getUniqueId().toString())) {
			player.setHealth(0.0d);
			entityUtil.damage(player, e.getCause(), Double.MAX_VALUE);
		}
	}
}
