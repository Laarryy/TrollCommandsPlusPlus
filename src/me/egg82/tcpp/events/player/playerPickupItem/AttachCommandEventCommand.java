package me.egg82.tcpp.events.player.playerPickupItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;
import ninja.egg82.plugin.utils.CommandUtil;

public class AttachCommandEventCommand extends EventCommand {
	//vars
	private INBTHelper nbtHelper = (INBTHelper) ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachCommandEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		ItemStack item = e.getItem().getItemStack();
		if (!nbtHelper.hasTag(item, "tcppCommand")) {
			return;
		}
		
		Player sender = CommandUtil.getPlayerByUuid((String) nbtHelper.getTag(item, "tcppSender"));
		if (sender != null) {
			CommandUtil.dispatchCommandAtPlayerLocation(sender, e.getPlayer(), (String) nbtHelper.getTag(item, "tcppCommand"));
		} else {
			if (CommandUtil.getOfflinePlayerByUuid((String) nbtHelper.getTag(item, "tcppSender")).isOp()) {
				CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), e.getPlayer(), (String) nbtHelper.getTag(item, "tcppCommand"));
			} else {
				Bukkit.dispatchCommand(e.getPlayer(), (String) nbtHelper.getTag(item, "tcppCommand"));
			}
		}
		
		nbtHelper.removeTag(item, "tcppSender");
		nbtHelper.removeTag(item, "tcppCommand");
	}
}
