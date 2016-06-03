package me.egg82.tcpp.ticks;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;
import com.egg82.utils.MathUtil;
import com.egg82.utils.Util;

import me.egg82.tcpp.enums.PluginServiceType;

public class HauntTickCommand extends Command {
	//vars
	private IRegistry hauntRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.HAUNT_REGISTRY);
	private Sound[] sounds = null;
	
	//constructor
	public HauntTickCommand() {
		super();
		Object[] enums = Util.getStaticFields(Sound.class);
		sounds = Arrays.copyOf(enums, enums.length, Sound[].class);
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = hauntRegistry.registryNames();
		for (String name : names) {
			haunt((Player) hauntRegistry.getRegister(name));
		}
	}
	private void haunt(Player player) {
		if (Math.random() <= 0.1d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, (float) MathUtil.random(1.0d,  10.0d));
		}
	}
}
