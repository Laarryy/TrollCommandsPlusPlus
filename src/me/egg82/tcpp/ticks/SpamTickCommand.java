package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;
import com.egg82.utils.MathUtil;

import me.egg82.tcpp.enums.PluginServiceType;

public class SpamTickCommand extends Command {
	//vars
	private IRegistry spamRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SPAM_REGISTRY);
	private static final char [] subset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+{}|:\"<>?`-=[]\\;',./".toCharArray();
	
	//constructor
	public SpamTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = spamRegistry.registryNames();
		for (String name : names) {
			((Player) spamRegistry.getRegister(name)).sendMessage(randString(MathUtil.fairRoundedRandom(15, 50)));
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
