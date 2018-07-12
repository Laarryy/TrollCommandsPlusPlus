package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.registries.AnnoyRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.filters.EnumFilter;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class AnnoyTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> annoyRegistry = ServiceLocator.getService(AnnoyRegistry.class);
	
	private Sound[] sounds = null;
	
	//constructor
	public AnnoyTickCommand() {
		super(0L, 20L);
		
		EnumFilter<Sound> soundFilterHelper = new EnumFilter<Sound>(Sound.class);
		sounds = soundFilterHelper.filter(
			soundFilterHelper.filter(
				soundFilterHelper.getAllTypes(),
			"villager", true),
			"zombie", false);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : annoyRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.2d) {
			player.playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, (float) MathUtil.random(0.5d,  2.0d));
		}
	}
}
