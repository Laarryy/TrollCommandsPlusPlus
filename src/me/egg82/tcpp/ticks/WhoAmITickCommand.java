package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class WhoAmITickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.WHO_AM_I_REGISTRY);
	private static final char[] subset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}|:\"<>?`-=[]\\;',./".toCharArray();
	
	//constructor
	public WhoAmITickCommand() {
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
		
		if (Math.random() <= 0.2) {
			//Yeah, they're going to be different. I thought about it, and I like this more
			player.setDisplayName(randString(MathUtil.fairRoundedRandom(5, 16)));
			player.setPlayerListName(randString(MathUtil.fairRoundedRandom(5, 16)));
		}
	}
	private String randString(int length) {
		char buffer[] = new char[length];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = subset[MathUtil.fairRoundedRandom(0, subset.length - 1)];
		}
		return new String(buffer);
	}
}
