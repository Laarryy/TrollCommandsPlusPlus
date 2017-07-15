package me.egg82.tcpp.events.entity.entityDamageByEntity;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.egg82.tcpp.services.LagEntityRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<EntityDamageByEntityEvent> {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IRegistry lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagEntityRegistry = ServiceLocator.getService(LagEntityRegistry.class);
	
	//constructor
	public LagEventCommand(EntityDamageByEntityEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}
		
		Entity entity = event.getEntity();
		String uuid = entity.getUniqueId().toString();
		
		if (lagEntityRegistry.hasRegister(uuid)) {
			return;
		}
		
		if (!(entity instanceof Damageable)) {
			return;
		}
		
		Player player = (Player) event.getDamager();
		
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		lagEntityRegistry.setRegister(uuid, Entity.class, entity);
		
		event.setCancelled(true);
		
		// Manually doing the event after a random interval
		TaskUtil.runSync(new Runnable() {
			public void run() {
				// Cause the entity damage
				entityUtil.damage(player, (Damageable) entity, event.getCause(), event.getDamage());
				lagEntityRegistry.setRegister(uuid, Entity.class, null);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
