package me.egg82.tcpp.events.custom;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class LagBlockBreakEvent extends BlockBreakEvent {
	//vars
	
	//constructor
	public LagBlockBreakEvent(Block theBlock, Player player) {
		super(theBlock, player);
	}
	
	//public
	
	//private
	
}
