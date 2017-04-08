package me.egg82.tcpp.events.custom;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class LagPlayerPickupItemEvent extends PlayerPickupItemEvent {
	//vars
	
	//constructor
	public LagPlayerPickupItemEvent(Player player, Item item, int remaining) {
		super(player, item, remaining);
	}
	
	//public
	
	//private
	
}
