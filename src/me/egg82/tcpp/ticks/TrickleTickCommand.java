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
		
		if (Math.random() <= 0.1d) {
			int currentExp = (int) Math.floor(player.getExpToLevel() * player.getExp());
			int currentLevel = player.getLevel();
			
			if (currentLevel == 0 && currentExp == 0) {
				return;
			}
			
			int droppedExp = MathUtil.fairRoundedRandom(1, 10);
			
			if (droppedExp > currentExp) {
				if (currentLevel > 0) {
					player.setLevel(currentLevel - 1);
					
					droppedExp -= currentExp;
					currentExp = player.getExpToLevel();
					double newExp = ((double) currentExp - (double) droppedExp) / (double) player.getExpToLevel();
					player.setExp((float) newExp);
				} else {
					droppedExp = currentExp;
					player.setExp(0.0f);
				}
			} else if (droppedExp == currentExp) {
				player.setExp(0.0f);
			} else {
				double newExp = ((double) currentExp - (double) droppedExp) / (double) player.getExpToLevel();
				player.setExp((float) newExp);
			}
			
			player.getWorld().spawn(BlockUtil.getTopWalkableBlock(LocationUtil.getLocationBehind(player.getLocation(), MathUtil.random(3.0d, 5.0d))), ExperienceOrb.class).setExperience(droppedExp);
		}
	}
}
