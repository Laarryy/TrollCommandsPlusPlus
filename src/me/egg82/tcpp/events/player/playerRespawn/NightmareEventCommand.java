package me.egg82.tcpp.events.player.playerRespawn;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.protocol.IFakeLivingEntity;

public class NightmareEventCommand extends EventCommand<PlayerRespawnEvent> {
	//vars
	private IRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareEventCommand(PlayerRespawnEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		UUID uuid = event.getPlayer().getUniqueId();
		
		if (!nightmareRegistry.hasRegister(uuid)) {
			return;
		}
		
		for (IFakeLivingEntity entity : (Collection<IFakeLivingEntity>) nightmareRegistry.getRegister(uuid)) {
			entity.teleportTo(event.getRespawnLocation());
		}
	}
}
