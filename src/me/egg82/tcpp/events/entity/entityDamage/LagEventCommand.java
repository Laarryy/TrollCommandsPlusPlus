package me.egg82.tcpp.events.entity.entityDamage;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.services.registries.LagEntityRegistry;
import me.egg82.tcpp.services.registries.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<EntityDamageEvent> {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry<UUID> lagEntityRegistry = ServiceLocator.getService(LagEntityRegistry.class);
	
	//constructor
	public LagEventCommand(EntityDamageEvent event) {
		super(event);
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
