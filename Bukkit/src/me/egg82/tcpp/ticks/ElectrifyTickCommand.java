package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.ElectrifyRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class ElectrifyTickCommand extends TickCommand {
	//vars
	private IVariableRegistry<UUID> electrifyRegistry = ServiceLocator.getService(ElectrifyRegistry.class);
	
	//constructor
	public ElectrifyTickCommand() {
		super(15L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : electrifyRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		int numLightning = MathUtil.fairRoundedRandom(1, 3);
		Location playerLocation = player.getLocation().clone();
		World world = player.getWorld();
		
		for (int i = 0; i < numLightning; i++) {
			world.strikeLightning(playerLocation);
		}
	}
}