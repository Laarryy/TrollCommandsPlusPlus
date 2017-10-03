package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.AnnoyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.reflection.type.TypeFilterHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class AnnoyTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> annoyRegistry = ServiceLocator.getService(AnnoyRegistry.class);
	
	private Sound[] sounds = null;
	
	//constructor
	public AnnoyTickCommand() {
		super();
		ticks = 20L;
		
		TypeFilterHelper<Sound> soundFilterHelper = new TypeFilterHelper<Sound>(Sound.class);
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
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, 1.0f);
		}
	}
}
