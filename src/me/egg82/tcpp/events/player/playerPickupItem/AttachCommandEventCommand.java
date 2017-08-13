package me.egg82.tcpp.events.player.playerPickupItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
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
		
		INBTCompound itemCompound = nbtHelper.getCompound(event.getItem().getItemStack());
		if (!itemCompound.hasTag("tcppCommand")) {
			return;
		}
		
		Player sender = CommandUtil.getPlayerByUuid(itemCompound.getString("tcppSender"));
		if (sender != null) {
			CommandUtil.dispatchCommandAtPlayerLocation(sender, event.getPlayer(), itemCompound.getString("tcppCommand"));
		} else {
			if (CommandUtil.getOfflinePlayerByUuid(itemCompound.getString("tcppSender")).isOp()) {
				CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), event.getPlayer(), itemCompound.getString("tcppCommand"));
			} else {
				Bukkit.dispatchCommand(event.getPlayer(), itemCompound.getString("tcppCommand"));
			}
		}
		
		itemCompound.removeTag("tcppSender");
		itemCompound.removeTag("tcppCommand");
	}
}
