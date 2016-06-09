package me.egg82.tcpp.events.individual.playerMoveEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.TrollCommandsPlusPlus;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class InfinityEventCommand extends EventCommand {
	//vars
	private IRegistry infinityRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.INFINITY_REGISTRY);
	
	//constructor
	public InfinityEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		Player player = e.getPlayer();
		Location pl = player.getLocation();
		Location l = pl.getWorld().getHighestBlockAt(pl).getLocation().add(0.0d, 10.0d, 0.0d);
		
		if (infinityRegistry.contains(player.getName().toLowerCase())) {
			if (pl.getBlockY() <= l.getBlockY()) {
				Location loc = pl.clone().add(0.0d, 30.0d, 0.0d);
				
				//hacky fix, otherwise server throws a fit with a StackOverflowException
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TrollCommandsPlusPlus.getInstance(), new Runnable() {
					public void run() {
						player.teleport(loc);
					}
				}, 1);
			}
		}
	}
}
