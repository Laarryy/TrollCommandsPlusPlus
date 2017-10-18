package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.NightmareRegistry;
import ninja.egg82.patterns.IObjectPool;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.protocol.core.IFakeLivingEntity;

public class NightmareEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
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
		
		IObjectPool<IFakeLivingEntity> entities = nightmareRegistry.getRegister(uuid, IObjectPool.class);
		if (entities != null) {
			for (IFakeLivingEntity e : entities) {
				e.removePlayer(player);
			}
		}
	}
}
