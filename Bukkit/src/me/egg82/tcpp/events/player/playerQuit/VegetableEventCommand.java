package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.registries.VegetableRegistry;
import me.egg82.tcpp.util.VegetableHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class VegetableEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IVariableRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	
	private VegetableHelper vegetableHelper = ServiceLocator.getService(VegetableHelper.class);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (vegetableRegistry.hasRegister(uuid)) {
			vegetableHelper.unvegetable(uuid, player);
		}
	}
}
