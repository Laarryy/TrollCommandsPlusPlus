package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class PotatoCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public PotatoCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_POTATO, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				items[i] = new ItemStack(Material.POTATO_ITEM, items[i].getAmount());
			}
		}
		
		inv.setContents(items);
		
		sender.sendMessage(name + "'s inventory is now potato.");
	}
}
