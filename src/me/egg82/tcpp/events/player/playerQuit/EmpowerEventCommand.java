package me.egg82.tcpp.events.player.playerQuit;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import ninja.egg82.plugin.commands.EventCommand;

public class EmpowerEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	
	//constructor
	public EmpowerEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		
		if (player.hasPotionEffect(PotionEffectType.ABSORPTION)
		&& player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)
		&& player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)
		&& player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)
		&& player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)
		&& player.hasPotionEffect(PotionEffectType.SPEED)) {
			// Disempower them
			player.removePotionEffect(PotionEffectType.ABSORPTION);
			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
			player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.removePotionEffect(PotionEffectType.SPEED);
		}
	}
}
