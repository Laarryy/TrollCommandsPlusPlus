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

public class AnnoyTickCommand extends TickCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.ANNOY_REGISTRY);
	ISoundUtil soundUtil = (ISoundUtil) ((IRegistry) ServiceLocator.getService(SpigotServiceType.REFLECT_REGISTRY)).getRegister(SpigotReflectType.SOUND);
	private Sound[] sounds = null;
	
	//constructor
	public AnnoyTickCommand() {
		super();
		ticks = 20l;
		sounds = soundUtil.filter(soundUtil.filter(soundUtil.getAllSounds(), "villager", true), "zombie", false);
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
		
		if (Math.random() <= 0.2d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, 1.0f);
		}
	}
}
