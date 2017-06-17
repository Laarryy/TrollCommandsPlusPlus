package me.egg82.tcpp.events.player.playerItemConsume;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.VomitRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.player.IPlayerUtil;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class VomitEventCommand extends EventCommand {
	//vars
	IRegistry vomitRegistry = (IRegistry) ServiceLocator.getService(VomitRegistry.class);
	
	IPlayerUtil playerUtil = (IPlayerUtil) ServiceLocator.getService(IPlayerUtil.class);
	
	//constructor
	public VomitEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerItemConsumeEvent e = (PlayerItemConsumeEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (vomitRegistry.hasRegister(player.getUniqueId().toString())) {
			ItemStack items = playerUtil.getItemInMainHand(player);
			
			ItemStack droppedItem = new ItemStack(items);
			droppedItem.setAmount(1);
			
			int itemsAmount = items.getAmount();
			
			if (itemsAmount == 1) {
				playerUtil.setItemInMainHand(player, null);
			} else {
				items.setAmount(itemsAmount - 1);
				playerUtil.setItemInMainHand(player, items);
			}
			
			player.getWorld().dropItemNaturally(BlockUtil.getTopAirBlock(LocationUtil.getLocationInFront(player.getLocation(), MathUtil.random(3.0d, 5.0d))), droppedItem);
			
			e.setCancelled(true);
		}
	}
}
