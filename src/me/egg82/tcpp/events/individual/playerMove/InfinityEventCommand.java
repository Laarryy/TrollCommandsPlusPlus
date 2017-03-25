package me.egg82.tcpp.events.individual.playerMove;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class InfinityEventCommand extends EventCommand {
	//vars
	private IRegistry infinityRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.INFINITY_REGISTRY);
	
	//constructor
	public InfinityEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		Player player = e.getPlayer();
		
		if (infinityRegistry.contains(player.getUniqueId().toString())) {
			Location pl = player.getLocation();
			
			World world = pl.getWorld();
			Location l = pl.clone();
			l.setY(0.0d);
			
			for (int x = -1; x < 2; x++) {
				for (int z = -1; z < 2; z++) {
					Location t = pl.clone();
					t.add(x, 0.0d, z);
					
					Location lt = world.getHighestBlockAt(t).getLocation();
					if (lt.getY() > l.getY()) {
						l = lt.clone();
					}
				}
			}
			
			l.add(0.0d, 2.0d, 0.0d);
			
			if (pl.getBlockY() <= l.getBlockY()) {
				Location loc = pl.clone().add(0.0d, 30.0d, 0.0d);
				player.teleport(loc);
			}
		}
	}
}
