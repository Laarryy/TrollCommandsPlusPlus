package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.LavaBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LavaBuildEventCommand extends EventCommand {
	//vars
	private IRegistry lavaBuildRegistry = (IRegistry) ServiceLocator.getService(LavaBuildRegistry.class);
	
	//constructor
	public LavaBuildEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (lavaBuildRegistry.hasRegister(uuid)) {
			e.getBlock().setType(Material.LAVA);
			lavaBuildRegistry.setRegister(uuid, Player.class, null);
		}
	}
}
