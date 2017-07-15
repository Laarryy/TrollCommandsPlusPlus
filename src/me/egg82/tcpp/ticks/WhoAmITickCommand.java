package me.egg82.tcpp.ticks;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.WhoAmIRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class WhoAmITickCommand extends TickCommand {
	//vars
	private IRegistry whoAmIRegistry = ServiceLocator.getService(WhoAmIRegistry.class);
	
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
			e(name, whoAmIRegistry.getRegister(name, Player.class));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.2) {
			OfflinePlayer[] players = Bukkit.getOfflinePlayers();
			OfflinePlayer p = players[MathUtil.fairRoundedRandom(0, players.length - 1)];
			
			if (p.isOnline()) {
				Player p2 = CommandUtil.getPlayerByUuid(p.getUniqueId());
				
				player.setDisplayName(p2.getDisplayName());
				player.setPlayerListName(p2.getPlayerListName());
				player.setCustomName(p2.getCustomName());
			} else {
				player.setDisplayName(p.getName());
				player.setPlayerListName(p.getName());
				player.setCustomName(p.getName());
			}
		}
	}
}
