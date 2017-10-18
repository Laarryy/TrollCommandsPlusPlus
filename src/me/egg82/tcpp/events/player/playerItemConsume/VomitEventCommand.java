package me.egg82.tcpp.events.player.playerItemConsume;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.registries.VomitRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class VomitEventCommand extends EventCommand<PlayerItemConsumeEvent> {
	//vars
	private IRegistry<UUID> vomitRegistry = ServiceLocator.getService(VomitRegistry.class);
	
	private IPlayerHelper playerUtil = ServiceLocator.getService(IPlayerHelper.class);
	
	//constructor
	public VomitEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (vomitRegistry.hasRegister(player.getUniqueId())) {
			ItemStack items = playerUtil.getItemInMainHand(player);
			
			if (items == null || items.getType() == Material.AIR) {
				items = event.getItem();
			}
			
			ItemStack droppedItem = new ItemStack(items);
			droppedItem.setAmount(1);
			
			int itemsAmount = items.getAmount();
			
			if (itemsAmount == 1) {
				playerUtil.setItemInMainHand(player, null);
			} else {
				items.setAmount(itemsAmount - 1);
				playerUtil.setItemInMainHand(player, items);
			}
			
			player.getWorld().dropItemNaturally(BlockUtil.getTopWalkableBlock(LocationUtil.getLocationInFront(player.getLocation(), MathUtil.random(3.0d, 5.0d))), droppedItem);
			
			event.setCancelled(true);
		}
	}
}
