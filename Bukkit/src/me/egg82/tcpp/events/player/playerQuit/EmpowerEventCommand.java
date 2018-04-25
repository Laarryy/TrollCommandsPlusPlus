package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.registries.EmpowerEntityRegistry;
import me.egg82.tcpp.services.registries.EmpowerRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class EmpowerEventCommand extends EventCommand<PlayerQuitEvent> {
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
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		empowerRegistry.removeRegister(uuid);
		
		if (empowerEntityRegistry.hasRegister(uuid)) {
			player.removePotionEffect(PotionEffectType.ABSORPTION);
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.removePotionEffect(PotionEffectType.SPEED);
			
			empowerEntityRegistry.removeRegister(uuid);
		}
	}
}
