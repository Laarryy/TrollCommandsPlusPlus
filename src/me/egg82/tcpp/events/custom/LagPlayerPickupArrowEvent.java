package me.egg82.tcpp.events.custom;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class LagPlayerPickupArrowEvent extends PlayerPickupArrowEvent {
	//vars
	
	//constructor
	public LagPlayerPickupArrowEvent(Player player, Item item, Arrow arrow) {
		super(player, item, arrow);
	}
	
	//public
	
	//private
	
}
