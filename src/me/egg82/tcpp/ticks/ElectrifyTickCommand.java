package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;
import com.egg82.utils.MathUtil;

import me.egg82.tcpp.enums.PluginServiceType;

public class ElectrifyTickCommand extends Command {
	//vars
	private IRegistry electrifyRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.ELECTRIFY_REGISTRY);
	
	//constructor
	public ElectrifyTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = electrifyRegistry.registryNames();
		for (String name : names) {
			strike((Player) electrifyRegistry.getRegister(name));
		}
	}
	
	private void strike(Player player) {
		int rand = (int) (MathUtil.random(2.0d, 5.0d));
		Location loc = player.getLocation();
		World world = player.getWorld();
		for (int i = 0; i < rand; i++) {
			world.strikeLightning(loc);
		}
	}
}