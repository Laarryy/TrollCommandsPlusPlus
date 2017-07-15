package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.AloneRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AloneEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry aloneRegistry = ServiceLocator.getService(AloneRegistry.class);
	
	//constructor
	public AloneEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (aloneRegistry.hasRegister(uuid)) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.hidePlayer(p);
			}
			aloneRegistry.setRegister(uuid, Player.class, player);
		} else {
			String[] names = aloneRegistry.getRegistryNames();
			for (String n : names) {
				Player p = aloneRegistry.getRegister(n, Player.class);
				if (p != null) {
					p.hidePlayer(player);
				}
			}
		}
	}
}
