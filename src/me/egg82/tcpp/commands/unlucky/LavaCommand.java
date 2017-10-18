package me.egg82.tcpp.commands.unlucky;

import org.bukkit.Material;

import me.egg82.tcpp.core.LuckyCommand;

public class LavaCommand extends LuckyCommand {
	//vars
	
	//constructor
	public LavaCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		player.getWorld().getBlockAt(player.getEyeLocation()).setType(Material.LAVA);
		player.getWorld().getBlockAt(player.getLocation()).setType(Material.WEB);
	}
}
