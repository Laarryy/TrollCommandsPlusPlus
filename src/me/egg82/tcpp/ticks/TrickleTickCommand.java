package me.egg82.tcpp.ticks;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.TrickleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class TrickleTickCommand extends TickCommand {
	//vars
	private IRegistry trickleRegistry = ServiceLocator.getService(TrickleRegistry.class);
	
	//constructor
	public TrickleTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = trickleRegistry.getRegistryNames();
		for (String name : names) {
			e(name, trickleRegistry.getRegister(name, Player.class));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		int currentExp = (int) (player.getExpToLevel() * player.getExp());
		int currentLevel = player.getLevel();
		
		if (currentExp == 0 && currentLevel > 0) {
			player.setLevel(currentLevel - 1);
			player.setExp(1);
			currentExp = player.getExpToLevel();
		}
		
		if (Math.random() <= 0.1 && currentExp > 0) {
			int droppedExp = 0;
			if (currentExp > 10) {
				droppedExp = MathUtil.fairRoundedRandom(1, 10);
				player.setExp((((float) currentExp) - ((float) droppedExp)) / ((float) player.getExpToLevel()));
			} else {
				droppedExp = currentExp + 1;
				player.setLevel(currentLevel - 1);
				player.setExp(1.0f - ((((float) currentExp) - ((float) droppedExp)) / ((float) player.getExpToLevel())));
			}
			
			player.getWorld().spawn(BlockUtil.getTopWalkableBlock(LocationUtil.getLocationBehind(player.getLocation(), MathUtil.random(3.0d, 5.0d))), ExperienceOrb.class).setExperience(droppedExp);
		}
	}
}
