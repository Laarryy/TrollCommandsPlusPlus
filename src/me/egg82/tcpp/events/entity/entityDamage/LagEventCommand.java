package me.egg82.tcpp.events.entity.entityDamage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.LagEntityRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityUtil;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IEntityUtil entityUtil = (IEntityUtil) ServiceLocator.getService(IEntityUtil.class);
	
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagEntityRegistry = (IRegistry) ServiceLocator.getService(LagEntityRegistry.class);
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
		String uuid = player.getUniqueId().toString();
		
		if (lagEntityRegistry.hasRegister(uuid)) {
			return;
		}
		if (!lagRegistry.hasRegister(uuid)) {
			return;
		}
		
		lagEntityRegistry.setRegister(uuid, Entity.class, player);
		
		e.setCancelled(true);
		
		// Manually doing the event after a random interval
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
			public void run() {
				// Cause the player damage
				entityUtil.damage(player, e.getCause(), e.getDamage());
				lagEntityRegistry.setRegister(uuid, Entity.class, null);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
