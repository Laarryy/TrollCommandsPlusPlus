package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class ElectrifyTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.ELECTRIFY_REGISTRY);
	
	//constructor
	public ElectrifyTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = reg.registryNames();
		for (String name : names) {
			e((Player) reg.getRegister(name));
		}
	}
	
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		int rand = (int) (MathUtil.random(2.0d, 5.0d));
		Location loc = player.getLocation();
		World world = player.getWorld();
		for (int i = 0; i < rand; i++) {
			world.strikeLightning(loc);
		}
	}
}