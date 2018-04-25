package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.RandomSpeedRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class RandomSpeedTickCommand extends TickCommand {
	//vars
	private IVariableRegistry<UUID> randomSpeedRegistry = ServiceLocator.getService(RandomSpeedRegistry.class);
	
	//constructor
	public RandomSpeedTickCommand() {
		super(55L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : randomSpeedRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.15d) {
			player.setWalkSpeed((float) MathUtil.random(0.05d, 1.0d));
			player.setFlySpeed((float) MathUtil.random(0.05d, 1.0d));
		}
	}
}