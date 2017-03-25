package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class SquidTickCommand extends TickCommand {
	//vars
	private IRegistry squidRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SQUID_REGISTRY);
	private IRegistry squidInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SQUID_INTERN_REGISTRY);
	
	//constructor
	public SquidTickCommand() {
		super();
		ticks = 10l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = squidRegistry.registryNames();
		for (String name : names) {
			e((Player) squidRegistry.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		int rand = (int) (MathUtil.random(5.0d, 10.0d));
		Location pl = player.getLocation().clone();
		for (int i = 0; i < rand; i++) {
			Squid s = player.getWorld().spawn(pl.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Squid.class);
			squidInternRegistry.setRegister(s.getUniqueId().toString(), s);
		}
	}
}
