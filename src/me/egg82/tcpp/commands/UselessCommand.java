package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class UselessCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public UselessCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_USELESS, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		ItemStack[] items = player.getInventory().getContents();
		ItemMeta meta = null;
		
		for (ItemStack i : items) {
			if (i == null) {
				continue;
			}
			
			meta = i.getItemMeta();
			meta.setDisplayName("Useless");
			i.setItemMeta(meta);
		}
		
		sender.sendMessage(name + "'s items are now all \"Useless\"!");
	}
}
