package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.SpamRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.StringUtil;

public class SpamTickCommand extends TickCommand {
	//vars
	private IRegistry spamRegistry = (IRegistry) ServiceLocator.getService(SpamRegistry.class);
	
	//constructor
	public SpamTickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = spamRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) spamRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		if (Math.random() < 0.35d) {
			player.sendMessage(StringUtil.randomString(MathUtil.fairRoundedRandom(15, 50)));
		}
	}
}
