package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.VegetableRegistry;
import me.egg82.tcpp.util.VegetableHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> vegetableRegistry = ServiceLocator.getService(VegetableRegistry.class);
	
	private VegetableHelper vegetableHelper = ServiceLocator.getService(VegetableHelper.class);
	
	//constructor
	public VegetableEventCommand(PlayerQuitEvent event) {
		super(event);
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
