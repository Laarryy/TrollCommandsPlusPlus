package me.egg82.tcpp.ticks;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.AnnoyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.SoundHelper;
import ninja.egg82.utils.MathUtil;

public class AnnoyTickCommand extends TickCommand {
	//vars
	private IRegistry annoyRegistry = (IRegistry) ServiceLocator.getService(AnnoyRegistry.class);
	
	private SoundHelper soundHelper = (SoundHelper) ServiceLocator.getService(SoundHelper.class);
	private Sound[] sounds = null;
	
	//constructor
	public AnnoyTickCommand() {
		super();
		ticks = 20L;
		sounds = soundHelper.filter(soundHelper.filter(soundHelper.getAllSounds(), "villager", true), "zombie", false);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = annoyRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) annoyRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if(!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.2d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, 1.0f);
		}
	}
}
