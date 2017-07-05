package me.egg82.tcpp.events.block.blockBreak;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.BlockUtil;

public class AttachCommandEventCommand extends EventCommand {
	//vars
	private INBTHelper nbtHelper = (INBTHelper) ServiceLocator.getService(INBTHelper.class);
	private IPlayerHelper playerHelper = (IPlayerHelper) ServiceLocator.getService(IPlayerHelper.class);
	
	//constructor
	public AttachCommandEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Block block = e.getBlock();
		Player player = e.getPlayer();
		
		if (!nbtHelper.supportsBlocks()) {
			return;
		}
		if (!nbtHelper.hasTag(block, "tcppCommand")) {
			return;
		}
		
		e.setCancelled(true);
		
		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
			Collection<ItemStack> drops = e.getBlock().getDrops(playerHelper.getItemInMainHand(player));
			for (ItemStack item : drops) {
				nbtHelper.addTag(item, "tcppCommand", nbtHelper.getTag(block, "tcppCommand"));
				player.getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
			}
		} else {
			Bukkit.dispatchCommand(player, (String) nbtHelper.getTag(block, "tcppCommand"));
		}
		
		BlockUtil.setBlock(block, new BlockData(null, null, Material.AIR), true);
	}
}
