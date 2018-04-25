package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Entity;

import me.egg82.tcpp.services.registries.SpartaArrowRegistry;
import me.egg82.tcpp.services.registries.SquidDeathRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.TickCommand;

public class ClearEntityTickCommand extends TickCommand {
	//vars
	private IVariableRegistry<UUID> squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	private IVariableRegistry<UUID> spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);
	
	//constructor
	public ClearEntityTickCommand() {
		super(50L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : squidDeathRegistry.getKeys()) {
			e(squidDeathRegistry, key, squidDeathRegistry.getRegister(key, Entity.class));
		}
		
		for (UUID key : spartaArrowRegistry.getKeys()) {
			e(spartaArrowRegistry, key, spartaArrowRegistry.getRegister(key, Entity.class));
		}
	}
	private void e(IVariableRegistry<UUID> registry, UUID uuid, Entity entity) {
		if (entity.getTicksLived() >= 100L) {
			entity.remove();
			registry.removeRegister(uuid);
		}
	}
}
