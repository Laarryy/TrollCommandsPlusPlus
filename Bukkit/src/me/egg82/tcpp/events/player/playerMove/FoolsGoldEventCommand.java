package me.egg82.tcpp.events.player.playerMove;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.registries.FoolsGoldRegistry;
import me.egg82.tcpp.util.FoolsGoldHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FoolsGoldEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IRegistry<UUID> foolsGoldRegistry = ServiceLocator.getService(FoolsGoldRegistry.class);
	
	private FoolsGoldHelper foolsGoldHelper = ServiceLocator.getService(FoolsGoldHelper.class);
	
	//constructor
	public FoolsGoldEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (foolsGoldRegistry.hasRegister(uuid)) {
			foolsGoldHelper.updatePlayer(uuid, player, event.getFrom(), event.getTo());
		}
	}
}
