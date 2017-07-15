package me.egg82.tcpp.events.player.playerPickupItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;
import ninja.egg82.plugin.utils.CommandUtil;

public class AttachCommandEventCommand extends EventCommand<PlayerPickupItemEvent> {
	//vars
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachCommandEventCommand(PlayerPickupItemEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack item = event.getItem().getItemStack();
		if (!nbtHelper.hasTag(item, "tcppCommand")) {
			return;
		}
		
		Player sender = CommandUtil.getPlayerByUuid(nbtHelper.getTag(item, "tcppSender", String.class));
		if (sender != null) {
			CommandUtil.dispatchCommandAtPlayerLocation(sender, event.getPlayer(), nbtHelper.getTag(item, "tcppCommand", String.class));
		} else {
			if (CommandUtil.getOfflinePlayerByUuid(nbtHelper.getTag(item, "tcppSender", String.class)).isOp()) {
				CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), event.getPlayer(), nbtHelper.getTag(item, "tcppCommand", String.class));
			} else {
				Bukkit.dispatchCommand(event.getPlayer(), nbtHelper.getTag(item, "tcppCommand", String.class));
			}
		}
		
		nbtHelper.removeTag(item, "tcppSender");
		nbtHelper.removeTag(item, "tcppCommand");
	}
}
