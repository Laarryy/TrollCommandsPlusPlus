package me.egg82.tcpp.ticks;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.EffectRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class EffectTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> effectRegistry = ServiceLocator.getService(EffectRegistry.class);
	
	//constructor
	public EffectTickCommand() {
		super();
		ticks = 100L;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = effectRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key), effectRegistry.getRegister(key, List.class));
		}
	}
	private void e(UUID uuid, Player player, List<PotionEffectType> effects) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		for (PotionEffectType e : effects) {
			player.addPotionEffect(new PotionEffect(e, Integer.MAX_VALUE, 5), true);
		}
	}
}
