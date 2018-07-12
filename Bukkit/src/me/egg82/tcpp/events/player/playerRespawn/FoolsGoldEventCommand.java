package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.registries.FoolsGoldRegistry;
import me.egg82.tcpp.util.FoolsGoldHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class FoolsGoldEventCommand extends EventHandler<PlayerRespawnEvent> {
	//vars
	private IVariableRegistry<UUID> foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
	
	private FoolsGoldHelper foolsGoldHelper = ServiceLocator.getService(FoolsGoldHelper.class);
	
	//constructor
	public FoolsGoldEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (foolsGoldRegistry.hasRegister(uuid)) {
			foolsGoldHelper.updatePlayer(uuid, player, event.getRespawnLocation());
		}
	}
}
