package me.egg82.tcpp.events.custom;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LagPlayerInteractEvent extends PlayerInteractEvent {
	//vars
	
	//constructor
	public LagPlayerInteractEvent(Player who, Action action, ItemStack item, Block clickedBlock, BlockFace clickedFace, EquipmentSlot hand) {
		super(who, action, item, clickedBlock, clickedFace, hand);
	}
	
	//public
	
	//private
	
}
