package me.egg82.tcpp.commands.unlucky;

import me.egg82.tcpp.core.LuckyCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class TeleportCommand extends LuckyCommand {
	//vars
	
	//constructor
	public TeleportCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		player.teleport(BlockUtil.getTopWalkableBlock(LocationUtil.getRandomPointAround(player.getLocation(), MathUtil.random(150.0d, 300.0d))));
	}
}
