package me.egg82.tcpp.events.player.playerQuit;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.services.VegetableRegistry;
import me.egg82.tcpp.util.VegetableHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(VegetableRegistry.class);
	
	private VegetableHelper vegetableHelper = (VegetableHelper) ServiceLocator.getService(VegetableHelper.class);
	
	//constructor
	public VegetableEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerQuitEvent e = (PlayerQuitEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (vegetableRegistry.hasRegister(uuid)) {
			vegetableHelper.unvegetable(uuid, player);
		}
	}
}
