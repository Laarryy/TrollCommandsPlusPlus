package me.egg82.tcpp.events.player.playerJoin;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.registries.NightmareRegistry;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.protocol.core.IFakeLivingEntity;

public class NightmareEventCommand extends EventHandler<PlayerJoinEvent> {
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
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		IConcurrentDeque<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid, IConcurrentDeque.class);
		if (entities != null) {
			for (IFakeLivingEntity e : entities) {
				e.addPlayer(player);
			}
		}
	}
}
