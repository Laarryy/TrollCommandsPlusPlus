package me.egg82.tcpp.events.block.blockBreak;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;

public class AttachCommandEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	
	//constructor
	public AttachCommandEventCommand(BlockBreakEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Block block = event.getBlock();
		Player player = event.getPlayer();
		
		if (!nbtHelper.supportsBlocks()) {
			return;
		}
		if (!nbtHelper.hasTag(block, "tcppCommand")) {
			return;
		}
		
		event.setCancelled(true);
		
		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
			Collection<ItemStack> drops = event.getBlock().getDrops(playerHelper.getItemInMainHand(player));
			for (ItemStack item : drops) {
				nbtHelper.addTag(item, "tcppSender", nbtHelper.getTag(block, "tcppSender"));
				nbtHelper.addTag(item, "tcppCommand", nbtHelper.getTag(block, "tcppCommand"));
				player.getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
			}
		} else {
			Player sender = CommandUtil.getPlayerByUuid(nbtHelper.getTag(block, "tcppSender", String.class));
			if (sender != null) {
				CommandUtil.dispatchCommandAtPlayerLocation(sender, player, nbtHelper.getTag(block, "tcppCommand", String.class));
			} else {
				if (CommandUtil.getOfflinePlayerByUuid(nbtHelper.getTag(block, "tcppSender", String.class)).isOp()) {
					CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), player, nbtHelper.getTag(block, "tcppCommand", String.class));
				} else {
					Bukkit.dispatchCommand(player, nbtHelper.getTag(block, "tcppCommand", String.class));
				}
			}
		}
		
		nbtHelper.removeTag(block, "tcppSender");
		nbtHelper.removeTag(block, "tcppCommand");
		BlockUtil.setBlock(block, new BlockData(null, null, Material.AIR), true);
	}
}
