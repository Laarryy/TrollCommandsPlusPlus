package me.egg82.tcpp.ticks;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.enums.SpigotReflectType;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.reflection.sound.interfaces.ISoundUtil;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class HauntTickCommand extends TickCommand {
	//vars
	private IRegistry hauntRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.HAUNT_REGISTRY);
	
	private ISoundUtil soundUtil = (ISoundUtil) ((IRegistry) ServiceLocator.getService(SpigotServiceType.REFLECT_REGISTRY)).getRegister(SpigotReflectType.SOUND);
	private Sound[] sounds = null;
	
	//constructor
	public HauntTickCommand() {
		super();
		ticks = 20l;
		sounds = soundUtil.getAllSounds();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = hauntRegistry.registryNames();
		for (String name : names) {
			e((Player) hauntRegistry.getRegister(name));
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
