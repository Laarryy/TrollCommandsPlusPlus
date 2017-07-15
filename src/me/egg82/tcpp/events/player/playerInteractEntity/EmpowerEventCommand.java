package me.egg82.tcpp.events.player.playerInteractEntity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.EmpowerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class EmpowerEventCommand extends EventCommand<PlayerInteractEntityEvent> {
	//vars
	private IRegistry empowerRegistry = ServiceLocator.getService(EmpowerRegistry.class);
	
	//constructor
	public EmpowerEventCommand(PlayerInteractEntityEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (!empowerRegistry.hasRegister(uuid)) {
			return;
		}
		if (!(event.getRightClicked() instanceof LivingEntity)) {
			player.sendMessage(MessageType.NOT_LIVING);
			empowerRegistry.setRegister(uuid, Player.class, null);
			return;
		}
		
		LivingEntity entity = (LivingEntity) event.getRightClicked();
		
		if (entity instanceof Player) {
			if (CommandUtil.hasPermission((Player) entity, PermissionsType.IMMUNE)) {
				player.sendMessage(MessageType.PLAYER_IMMUNE);
				return;
			}
		}
		
		if (entity.hasPotionEffect(PotionEffectType.ABSORPTION)
			&& entity.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)
			&& entity.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)
			&& entity.hasPotionEffect(PotionEffectType.HEALTH_BOOST)
			&& entity.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)
			&& entity.hasPotionEffect(PotionEffectType.SPEED)) {
			// Disempower them
			entity.removePotionEffect(PotionEffectType.ABSORPTION);
			entity.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			entity.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			entity.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			entity.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			entity.removePotionEffect(PotionEffectType.REGENERATION);
			entity.removePotionEffect(PotionEffectType.SPEED);
			
			player.sendMessage(MessageType.DISEMPOWERED);
		} else {
			// Empower them
			entity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 5), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 5), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 5), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 5), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 5), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 5), true);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 5), true);
			
			player.sendMessage(MessageType.EMPOWERED);
		}
		
		empowerRegistry.setRegister(uuid, Player.class, null);
	}
}
