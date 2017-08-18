package me.egg82.tcpp.commands.unlucky;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.egg82.tcpp.core.LuckyCommand;

public class LavaCommand extends LuckyCommand {
	//vars
	
	//constructor
	public LavaCommand(Player player) {
		super(player);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		player.getWorld().getBlockAt(player.getEyeLocation()).setType(Material.LAVA);
		player.getWorld().getBlockAt(player.getLocation()).setType(Material.WEB);
	}
}
