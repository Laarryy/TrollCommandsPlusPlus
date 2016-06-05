package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class PotatoCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public PotatoCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_POTATO, new int[]{1}, new int[]{0})) {
			if (args.length == 1) {
				e(Bukkit.getPlayer(args[0]));
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		PlayerInventory inv = player.getInventory();
		ItemStack[] items = inv.getContents();
		
		for (int i = 0; i < items.length; i++) {
			//Material.AIR because.. Well, you never fucking know with Minecraft.
			if (items[i] != null && items[i].getType() != Material.AIR) {
				items[i] = new ItemStack(Material.POTATO_ITEM, items[i].getAmount());
			}
		}
		
		inv.setContents(items);
		
		sender.sendMessage(player.getName() + "'s inventory is now potato.");
	}
}
