package me.egg82.tcpp.ticks;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.Util;

public class HauntTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.HAUNT_REGISTRY);
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
		String[] names = reg.registryNames();
		for (String name : names) {
			e((Player) reg.getRegister(name));
		}
	}
	private void e(Player player) {
		if(player == null) {
			return;
		}
		
		if (Math.random() <= 0.1d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, (float) MathUtil.random(1.0d,  10.0d));
		}
	}
}
