package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.StringUtil;

public class WhoAmITickCommand extends TickCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.WHO_AM_I_REGISTRY);
	
	//constructor
	public WhoAmITickCommand() {
		super();
		ticks = 20l;
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
		
		if (Math.random() <= 0.2) {
			//Yeah, they're going to be different. I thought about it, and I like this more
			player.setDisplayName(StringUtil.randomString(MathUtil.fairRoundedRandom(5, 16)));
			player.setPlayerListName(StringUtil.randomString(MathUtil.fairRoundedRandom(5, 16)));
		}
	}
}
