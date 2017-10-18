package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.registries.NecroRegistry;
import ninja.egg82.disguise.reflection.IDisguiseHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;

public class NecroEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private IRegistry<UUID> necroRegistry = ServiceLocator.getService(NecroRegistry.class);
	
	private IDisguiseHelper disguiseHelper = ServiceLocator.getService(IDisguiseHelper.class);
	private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public NecroEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (!necroRegistry.hasRegister(uuid)) {
			return;
		}
		
		if (player.isInsideVehicle()) {
			if (player.getVehicle() instanceof Spider) {
				entityHelper.removePassenger(player.getVehicle(), player);
			}
		}
		disguiseHelper.undisguise(player);
		ItemStack[] inv = necroRegistry.removeRegister(uuid, ItemStack[].class);
		player.getInventory().setContents(inv);
	}
}
