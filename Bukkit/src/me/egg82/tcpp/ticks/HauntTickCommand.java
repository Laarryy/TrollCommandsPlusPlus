package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.HauntRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.reflection.type.TypeFilterHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class HauntTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> hauntRegistry = ServiceLocator.getService(HauntRegistry.class);
	
	private Sound[] sounds = null;
	
	//constructor
	public HauntTickCommand() {
		super();
		ticks = 20L;
		
		TypeFilterHelper<Sound> soundFilterHelper = new TypeFilterHelper<Sound>(Sound.class);
		sounds = soundFilterHelper.getAllTypes();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : hauntRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.1d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, (float) MathUtil.random(0.5d,  2.0d));
		}
	}
}
