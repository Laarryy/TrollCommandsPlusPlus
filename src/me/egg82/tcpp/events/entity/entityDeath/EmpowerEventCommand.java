package me.egg82.tcpp.events.entity.entityDeath;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.EmpowerEntityRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class EmpowerEventCommand extends EventCommand<EntityDeathEvent> {
	//vars
	private IRegistry<UUID> empowerEntityRegistry = ServiceLocator.getService(EmpowerEntityRegistry.class);
	
	//constructor
	public EmpowerEventCommand(EntityDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		LivingEntity entity = event.getEntity();
		UUID uuid = entity.getUniqueId();
		
		if (empowerEntityRegistry.hasRegister(uuid)) {
			entity.removePotionEffect(PotionEffectType.ABSORPTION);
			entity.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			entity.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			entity.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			entity.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			entity.removePotionEffect(PotionEffectType.REGENERATION);
			entity.removePotionEffect(PotionEffectType.SPEED);
			
			empowerEntityRegistry.removeRegister(uuid);
		}
	}
}
