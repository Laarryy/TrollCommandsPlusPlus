package me.egg82.tcpp.core;

import org.bukkit.entity.Player;

import ninja.egg82.patterns.SynchronousCommand;

public abstract class LuckyCommand extends SynchronousCommand {
	//vars
	protected Player player = null;
	
	//constructor
	public LuckyCommand() {
		super();
	}
	
	//public
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	//private
	
}
