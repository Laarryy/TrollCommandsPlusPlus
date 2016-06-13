package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class SquidTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.SQUID_REGISTRY);
	
	//constructor
	public SquidTickCommand() {
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
		
		int rand = (int) (MathUtil.random(5.0d, 10.0d));
		Location pl = player.getLocation().clone();
		for (int i = 0; i < rand; i++) {
			player.getWorld().spawn(pl.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Squid.class);
		}
	}
}
