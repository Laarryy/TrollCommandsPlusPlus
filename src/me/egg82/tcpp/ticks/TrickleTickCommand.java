package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import me.egg82.tcpp.registries.TrickleRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class TrickleTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> trickleRegistry = ServiceLocator.getService(TrickleRegistry.class);
	
	//constructor
	public TrickleTickCommand() {
		super(0L, 10L);
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
			
			while (droppedExp > currentExp) {
				if (currentLevel > 0) {
					player.setLevel(currentLevel - 1);
					
					droppedExp -= currentExp;
					currentExp = player.getExpToLevel();
				} else {
					droppedExp = currentExp;
				}
			}
			
			if (droppedExp == currentExp) {
				player.setExp(0.0f);
			} else {
				double newExp = ((double) currentExp - (double) droppedExp) / (double) player.getExpToLevel();
				player.setExp((float) newExp);
			}
			
			player.getWorld().spawn(BlockUtil.getHighestSolidBlock(LocationUtil.getLocationBehind(player.getLocation(), MathUtil.random(3.0d, 5.0d), false)).add(0.0d, 1.0d, 0.0d), ExperienceOrb.class).setExperience(droppedExp);
		}
	}
}
