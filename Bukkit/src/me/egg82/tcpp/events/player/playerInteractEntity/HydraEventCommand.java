package me.egg82.tcpp.events.player.playerInteractEntity;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.services.registries.HydraMobRegistry;
import me.egg82.tcpp.services.registries.HydraRegistry;
import ninja.egg82.events.VariableExpireEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;
import ninja.egg82.plugin.utils.LanguageUtil;

public class HydraEventCommand extends EventCommand<PlayerInteractEntityEvent> {
	//vars
	private IVariableRegistry<UUID> hydraRegistry = ServiceLocator.getService(HydraRegistry.class);
	private IVariableExpiringRegistry<UUID> hydraMobRegistry = ServiceLocator.getService(HydraMobRegistry.class);
	
	//constructor
	public HydraEventCommand() {
		super();
		
		hydraMobRegistry.onExpire().attach((s, e) -> onMobExpire(e));
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!hydraRegistry.hasRegister(uuid)) {
			return;
		}
		if (!(event.getRightClicked() instanceof LivingEntity)) {
			player.sendMessage(LanguageUtil.getString(LanguageType.NOT_LIVING));
			hydraRegistry.removeRegister(uuid);
			return;
		}
		
		LivingEntity entity = (LivingEntity) event.getRightClicked();
		
		if (entity instanceof Player) {
			player.sendMessage(LanguageUtil.getString(LanguageType.NOT_MOB));
		}
		
		UUID entityUuid = entity.getUniqueId();
		
		if (!hydraMobRegistry.hasRegister(entityUuid)) {
			hydraMobRegistry.setRegister(entityUuid, null);
			player.sendMessage(LanguageUtil.getString(LanguageType.HYDRA_ENABLED));
		} else {
			removeAll(getRoot(entityUuid));
			player.sendMessage(LanguageUtil.getString(LanguageType.HYDRA_DISABLED));
		}
		
		hydraRegistry.removeRegister(uuid);
	}
	
	private void removeAll(UUID entityUuid) {
		UUID[] keys = hydraMobRegistry.getKeys();
		for (UUID key : keys) {
			UUID val = hydraMobRegistry.getRegister(key, UUID.class);
			if (entityUuid.equals(val)) {
				removeAll(key);
			}
		}
		hydraMobRegistry.removeRegister(entityUuid);
	}
	private UUID getRoot(UUID entityUuid) {
		UUID parent = hydraMobRegistry.getRegister(entityUuid, UUID.class);
		return (parent != null) ? getRoot(parent) : entityUuid;
	}
	
	private void onMobExpire(VariableExpireEventArgs<UUID> e) {
		if (Bukkit.getEntity(e.getKey()) != null) {
			hydraMobRegistry.setRegister(e.getKey(), e.getValue());
		}
	}
}
