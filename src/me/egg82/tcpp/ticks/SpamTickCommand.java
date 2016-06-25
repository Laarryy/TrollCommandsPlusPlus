package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.StringUtil;

public class SpamTickCommand extends TickCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.SPAM_REGISTRY);
	
	//constructor
	public SpamTickCommand() {
		super();
		ticks = 10l;
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
		
		player.sendMessage(StringUtil.randomString(MathUtil.fairRoundedRandom(15, 50)));
	}
}
