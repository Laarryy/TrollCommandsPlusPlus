package me.egg82.tcpp.events.entity.entityDamageByEntity;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.LagEntityRegistry;
import me.egg82.tcpp.services.LagRegistry;
import net.md_5.bungee.api.ChatColor;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IEntityHelper entityUtil = (IEntityHelper) ServiceLocator.getService(IEntityHelper.class);
	
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagEntityRegistry = (IRegistry) ServiceLocator.getService(LagEntityRegistry.class);
	private JavaPlugin plugin = (JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin");
	
	//constructor
	public LagEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (e.getDamager().getType() != EntityType.PLAYER) {
			return;
		}
		
		Entity entity = e.getEntity();
		String uuid = entity.getUniqueId().toString();
		
		if (lagEntityRegistry.hasRegister(uuid)) {
			return;
		}
		
		if (!(entity instanceof Damageable)) {
			return;
		}
		
		Player player = (Player) e.getDamager();
		
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		lagEntityRegistry.setRegister(uuid, Entity.class, entity);
		
		e.setCancelled(true);
		
		// Manually doing the event after a random interval
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Causing damage to entity.");
				// Cause the entity damage
				entityUtil.damage(player, (Damageable) entity, e.getCause(), e.getDamage());
				lagEntityRegistry.setRegister(uuid, Entity.class, null);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
