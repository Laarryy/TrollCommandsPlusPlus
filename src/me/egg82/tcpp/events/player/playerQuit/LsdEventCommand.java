package me.egg82.tcpp.events.player.playerQuit;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.LsdRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Triplet;
import ninja.egg82.plugin.commands.EventCommand;

public class LsdEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> lsdRegistry = ServiceLocator.getService(LsdRegistry.class);
	
	//constructor
	public LsdEventCommand(PlayerQuitEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (lsdRegistry.hasRegister(uuid)) {
			Collection<Triplet<String, Integer, Integer>> bLocs = lsdRegistry.getRegister(uuid, Collection.class);
			synchronized (bLocs) {
				bLocs.clear();
			}
		}
	}
}
