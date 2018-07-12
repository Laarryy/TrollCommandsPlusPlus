package me.egg82.tcpp.events.player.playerInteractEntity;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.EmpowerEntityRegistry;
import me.egg82.tcpp.registries.EmpowerRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class EmpowerEventCommand extends EventHandler<PlayerInteractEntityEvent> {
	//vars
	private IVariableRegistry<UUID> empowerRegistry = ServiceLocator.getService(EmpowerRegistry.class);
	private IVariableRegistry<UUID> empowerEntityRegistry = ServiceLocator.getService(EmpowerEntityRegistry.class);
	
	//constructor
	public EmpowerEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!empowerRegistry.hasRegister(uuid)) {
			return;
		}
		if (!(event.getRightClicked() instanceof LivingEntity)) {
			player.sendMessage(ChatColor.RED + "The entity you have selected is neither a player nor a mob!");
			empowerRegistry.removeRegister(uuid);
			return;
		}
		
		LivingEntity entity = (LivingEntity) event.getRightClicked();
		
		if (entity instanceof Player) {
			if (entity.hasPermission(PermissionsType.IMMUNE)) {
				player.sendMessage(ChatColor.RED + "Player is immune.");
				return;
			}
		}
		
		UUID entityUuid = entity.getUniqueId();
		
		if (empowerEntityRegistry.hasRegister(entityUuid)) {
			entity.removePotionEffect(PotionEffectType.ABSORPTION);
			entity.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			entity.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			entity.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			entity.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			entity.removePotionEffect(PotionEffectType.REGENERATION);
			entity.removePotionEffect(PotionEffectType.SPEED);
			
			empowerEntityRegistry.removeRegister(entityUuid);
			player.sendMessage(ChatColor.GREEN + "The entity you have selected is now empowered!");
		} else {
			entity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 5, true, false), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 5, true, false), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 5, true, false), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 5, true, false), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5, true, false), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 5, true, false), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5, true, false), true);
			
			empowerEntityRegistry.setRegister(entityUuid, null);
			player.sendMessage(ChatColor.GREEN + "The entity you have selected is no longer empowered!");
		}
		
		empowerRegistry.removeRegister(uuid);
	}
}
