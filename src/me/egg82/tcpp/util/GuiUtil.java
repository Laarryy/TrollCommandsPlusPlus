package me.egg82.tcpp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.databases.CommandSearchDatabase;
import me.egg82.tcpp.registries.GuiRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.patterns.tuples.pair.Pair;
import ninja.egg82.sql.LanguageDatabase;

public class GuiUtil {
	//vars
	private static LanguageDatabase commandDatabase = null;
	private static HashMap<String, Pair<String, String>> commands = null;
	private static String[] allCommands = null;
	
	private static IVariableRegistry<String> guiRegistry = null;
	
	//constructor
	public GuiUtil() {
		
	}
	
	//public
	public static Inventory createInventory(Player permissionsPlayer, String search, int page) {
		if (commandDatabase == null) {
			commandDatabase = ServiceLocator.getService(CommandSearchDatabase.class);
		}
		if (commands == null) {
			commands = new HashMap<String, Pair<String, String>>();
			String[] list = ((String) ServiceLocator.getService(JavaPlugin.class).getDescription().getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
			
			for (String entry : list) {
				if (entry.contains("-= Available Commands =-")) {
					continue;
				}
				
				String usage = entry.substring(0, entry.indexOf(':')).trim();
				String command = usage.split(" ")[1];
				String description = entry.substring(entry.indexOf(':') + 1).trim();
				
				commands.put(command, new Pair<String, String>(usage, description));
			}
		}
		if (allCommands == null) {
			allCommands = commands.keySet().toArray(new String[0]);
			Arrays.sort(allCommands);
		}
		if (guiRegistry == null) {
			guiRegistry = ServiceLocator.getService(GuiRegistry.class);
		}
		
		if (page < 0) {
			page = 0;
		}
		
		Inventory retVal = Bukkit.createInventory(null, 45, "TrollCommands++");
		String[] commands = null;
		
		if (search == null || search.isEmpty()) {
			commands = allCommands;
		} else {
			commands = commandDatabase.getValues(commandDatabase.naturalLanguage(search, false, ' '), 0);
		}
		if (commands == null) {
			commands = new String[0];
		}
		
		int numCommands = Math.min(Math.max(0, commands.length - (page * 37)), 37);
		
		for (int i = 0; i < numCommands; i++) {
			if (i < 27) {
				retVal.setItem(i, getItemStack(permissionsPlayer, commands[page * 37 + i]));
			} else if (i >= 27 && i < 32) {
				retVal.setItem(i + 2, getItemStack(permissionsPlayer, commands[page * 37 + i]));
			} else {
				retVal.setItem(i + 6, getItemStack(permissionsPlayer, commands[page * 37 + i]));
			}
		}
		
		if (inventoryIsEmpty(retVal)) {
			retVal.setItem(22, getCloseItem());
		}
		
		if (commands.length > (page + 1) * 37) {
			retVal.setItem(44, getNextItem());
		}
		if (page != 0) {
			retVal.setItem(36, getPreviousItem());
		}
		
		return retVal;
	}
	
	//private
	private static ItemStack getItemStack(CommandSender sender, String command) {
		ItemStack item = null;
		
		if (guiRegistry.hasRegister(command)) {
			if (guiRegistry.hasRegister(command + "_data")) {
				item = new ItemStack(guiRegistry.getRegister(command, Material.class), 1, guiRegistry.getRegister(command + "_data", Short.class));
			} else {
				item = new ItemStack(guiRegistry.getRegister(command, Material.class));
			}
		} else {
			item = new ItemStack(Material.STONE);
		}
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName((sender.hasPermission("tcpp.command." + command) ? ChatColor.WHITE : ChatColor.RED) + "/" + command);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		
		ArrayList<String> lore = new ArrayList<String>();
		if (!sender.hasPermission("tcpp.command." + command)) {
			lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "You do not have permissions");
			lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "to run this command!");
		}
		lore.addAll(splitDescription(commands.get(command).getRight()));
		lore.add(ChatColor.YELLOW + commands.get(command).getLeft());
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	private static ItemStack getNextItem() {
		ItemStack item = new ItemStack(Material.STONE_BUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Next");
		item.setItemMeta(meta);
		return item;
	}
	private static ItemStack getPreviousItem() {
		ItemStack item = new ItemStack(Material.STONE_BUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Previous");
		item.setItemMeta(meta);
		return item;
	}
	private static ItemStack getCloseItem() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close");
		item.setItemMeta(meta);
		return item;
	}
	
	private static boolean inventoryIsEmpty(Inventory inv) {
		ItemStack[] contents = inv.getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] != null && contents[i].getType() != Material.AIR) {
				return false;
			}
		}
		return true;
	}
	
	private static ArrayList<String> splitDescription(String input) {
		ArrayList<String> retVal = new ArrayList<String>();
		
		while (input.length() > 0) {
			String currentRow = "";
			while (currentRow.length() < 25) {
				int space = input.indexOf(' ');
				
				if (space == -1) {
					currentRow += input;
					input = "";
					break;
				}
				
				currentRow += input.substring(0, space + 1);
				input = input.substring(space + 1);
			}
			currentRow = currentRow.trim();
			if (!currentRow.isEmpty()) {
				retVal.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + currentRow);
			}
		}
		
		return retVal;
	}
}
