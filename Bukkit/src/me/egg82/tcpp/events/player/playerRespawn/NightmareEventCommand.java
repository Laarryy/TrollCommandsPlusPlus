package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.registries.NightmareRegistry;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.protocol.core.IFakeLivingEntity;

public class NightmareEventCommand extends EventHandler<PlayerRespawnEvent> {
	//vars
	private IVariableRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		UUID uuid = event.getPlayer().getUniqueId();
		
		IConcurrentDeque<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid, IConcurrentDeque.class);
		
		if (entities != null) {
			for (IFakeLivingEntity e : entities) {
				e.teleportTo(event.getRespawnLocation());
			}
		}
	}
}
