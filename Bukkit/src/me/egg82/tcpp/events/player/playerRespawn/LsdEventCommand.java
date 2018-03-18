package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.registries.LsdRegistry;
import ninja.egg82.patterns.IObjectPool;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.Triplet;
import ninja.egg82.plugin.commands.EventCommand;

public class LsdEventCommand extends EventCommand<PlayerRespawnEvent> {
	//vars
	private IRegistry<UUID> lsdRegistry = ServiceLocator.getService(LsdRegistry.class);
	
	//constructor
	public LsdEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (lsdRegistry.hasRegister(uuid)) {
			IObjectPool<Triplet<String, Integer, Integer>> bLocs = lsdRegistry.getRegister(uuid, IObjectPool.class);
			bLocs.clear();
		}
	}
}
