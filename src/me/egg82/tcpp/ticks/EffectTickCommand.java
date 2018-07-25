package me.egg82.tcpp.ticks;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.registries.EffectRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class EffectTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> effectRegistry = ServiceLocator.getService(EffectRegistry.class);
	
	//constructor
	public EffectTickCommand() {
		super(0L, 100L);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : effectRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), effectRegistry.getRegister(key, List.class));
		}
	}
	private void e(Player player, List<PotionEffectType> effects) {
		if (player == null) {
			return;
		}
		
		for (PotionEffectType e : effects) {
			player.addPotionEffect(new PotionEffect(e, Integer.MAX_VALUE, 5), true);
		}
	}
}
