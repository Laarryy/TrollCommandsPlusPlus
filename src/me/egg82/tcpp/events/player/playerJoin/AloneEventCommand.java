package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.AloneRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AloneEventCommand extends EventCommand {
	//vars
	private IRegistry aloneRegistry = (IRegistry) ServiceLocator.getService(AloneRegistry.class);
	
	//constructor
	public AloneEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (aloneRegistry.hasRegister(uuid)) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				player.hidePlayer(p);
			}
			aloneRegistry.setRegister(uuid, Player.class, player);
		} else {
			String[] names = aloneRegistry.getRegistryNames();
			for (String n : names) {
				Player p = (Player) aloneRegistry.getRegister(n);
				if (p != null) {
					p.hidePlayer(player);
				}
			}
		}
	}
}
