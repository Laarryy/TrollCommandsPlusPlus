package me.egg82.tcpp.events.entity.entityShootBow;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.egg82.tcpp.services.SnowballFightRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SnowballFightEventCommand extends EventCommand {
	//vars
	private IRegistry snowballFightRegistry = (IRegistry) ServiceLocator.getService(SnowballFightRegistry.class);
	
	//constructor
	public SnowballFightEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		EntityShootBowEvent e = (EntityShootBowEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		
		Player player = (Player) e.getEntity();
		String uuid = player.getUniqueId().toString();
		
		if (snowballFightRegistry.hasRegister(uuid)) {
			Entity ent = e.getProjectile();
			Snowball b = e.getEntity().getWorld().spawn(player.getEyeLocation(), Snowball.class);
			
			b.setVelocity(ent.getVelocity());
			b.setShooter(player);
			e.setProjectile(b);
		}
	}
}
