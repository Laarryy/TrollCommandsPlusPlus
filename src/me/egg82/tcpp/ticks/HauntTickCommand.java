package me.egg82.tcpp.ticks;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.HauntRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.SoundHelper;
import ninja.egg82.utils.MathUtil;

public class HauntTickCommand extends TickCommand {
	//vars
	private IRegistry hauntRegistry = (IRegistry) ServiceLocator.getService(HauntRegistry.class);
	
	private SoundHelper soundHelper = (SoundHelper) ServiceLocator.getService(SoundHelper.class);
	private Sound[] sounds = null;
	
	//constructor
	public HauntTickCommand() {
		super();
		ticks = 20L;
		sounds = soundHelper.getAllSounds();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = hauntRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) hauntRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if(!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.1d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, (float) MathUtil.random(1.0d,  10.0d));
		}
	}
}
