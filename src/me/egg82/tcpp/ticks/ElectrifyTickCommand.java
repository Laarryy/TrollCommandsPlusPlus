package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.ElectrifyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;

public class ElectrifyTickCommand extends TickCommand {
	//vars
	IRegistry electrifyRegistry = (IRegistry) ServiceLocator.getService(ElectrifyRegistry.class);
	
	//constructor
	public ElectrifyTickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = electrifyRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) electrifyRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
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