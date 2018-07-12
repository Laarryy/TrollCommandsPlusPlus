package me.egg82.tcpp.events.entity.entityDamage;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.registries.LagEntityRegistry;
import me.egg82.tcpp.registries.LagRegistry;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventHandler<EntityDamageEvent> {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IVariableRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IVariableRegistry<UUID> lagEntityRegistry = ServiceLocator.getService(LagEntityRegistry.class);
	
	//constructor
	public LagEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		
		Player player = (Player) event.getEntity();
		UUID uuid = player.getUniqueId();
		
		if (lagEntityRegistry.hasRegister(uuid)) {
			return;
		}
		if (!lagRegistry.hasRegister(uuid)) {
			return;
		}
		
		lagEntityRegistry.setRegister(uuid, null);
		
		event.setCancelled(true);
		
		// Manually doing the event after a random interval
		TaskUtil.runSync(new Runnable() {
			public void run() {
				// Cause the player damage
				entityUtil.damage(player, event.getCause(), event.getDamage());
				lagEntityRegistry.removeRegister(uuid);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
