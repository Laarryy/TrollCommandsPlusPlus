package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.WhoAmIRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.StringUtil;

public class WhoAmITickCommand extends TickCommand {
	//vars
	private IRegistry whoAmIRegistry = (IRegistry) ServiceLocator.getService(WhoAmIRegistry.class);
	
	//constructor
	public WhoAmITickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = whoAmIRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) whoAmIRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.2) {
			//Yeah, they're going to be different. I thought about it, and I like this more
			player.setDisplayName(StringUtil.randomString(MathUtil.fairRoundedRandom(5, 8)));
			player.setPlayerListName(StringUtil.randomString(MathUtil.fairRoundedRandom(5, 8)));
			player.setCustomName(StringUtil.randomString(MathUtil.fairRoundedRandom(5, 8)));
		}
	}
}
