package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.TrickleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class TrickleTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> trickleRegistry = ServiceLocator.getService(TrickleRegistry.class);
	
	//constructor
	public TrickleTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : trickleRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		double expToLevel = player.getExpToLevel();
		double currentExp = expToLevel * player.getExp();
		int currentLevel = player.getLevel();
		
		if (currentExp == 0.0d && currentLevel > 0) {
			player.setLevel(currentLevel - 1);
			player.setExp(1);
			currentExp = player.getExpToLevel();
		}
		
		if (Math.random() <= 0.1d && currentExp > 0.0d) {
			int droppedExp = 0;
			if (currentExp > 10.0d) {
				droppedExp = MathUtil.fairRoundedRandom(1, 10);
				double newExp = (currentExp - droppedExp) / expToLevel; // Floating-point math sucks.
				player.setExp((float) newExp);
			} else {
				droppedExp = (int) Math.floor(currentExp + 1.0d);
				player.setLevel(currentLevel - 1);
				double newExp = 1.0d - ((currentExp - droppedExp) / expToLevel); // Floating-point math sucks.
				player.setExp((float) newExp);
			}
			
			player.getWorld().spawn(BlockUtil.getTopWalkableBlock(LocationUtil.getLocationBehind(player.getLocation(), MathUtil.random(3.0d, 5.0d))), ExperienceOrb.class).setExperience(droppedExp);
		}
	}
}
