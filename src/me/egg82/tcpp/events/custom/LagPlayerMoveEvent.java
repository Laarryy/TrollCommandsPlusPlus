package me.egg82.tcpp.events.custom;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class LagPlayerMoveEvent extends PlayerMoveEvent {
	//vars
	
	//constructor
	public LagPlayerMoveEvent(Player player, Location from, Location to) {
		super(player, from, to);
	}
	
	//public
	
	//private
	
}
