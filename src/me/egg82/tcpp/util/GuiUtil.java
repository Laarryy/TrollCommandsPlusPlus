package me.egg82.tcpp.util;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.GuiRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.startup.InitRegistry;

public class GuiUtil {
	//vars
	private static LanguageDatabase ldb = null;
	private static Map<String, Map<String, Object>> commands = null;
	private static String[] allCommands = null;
	
	private static IRegistry guiRegistry = null;
	
	//constructor
	public GuiUtil() {
		
	}
	
	//public
	public static Inventory createInventory(Player permissionsPlayer, String search, int page) {
		if (ldb == null) {
			ldb = (LanguageDatabase) ServiceLocator.getService(LanguageDatabase.class);
		}
		if (commands == null) {
			commands = ((PluginDescriptionFile) ((JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin")).getDescription()).getCommands();
		}
		if (allCommands == null) {
			allCommands = commands.keySet().toArray(new String[0]);
		}
		if (guiRegistry == null) {
			guiRegistry = (IRegistry) ServiceLocator.getService(GuiRegistry.class);
		}
		
		if (page < 0) {
			page = 0;
		}
		
		Inventory retVal = Bukkit.createInventory(null, 27, "TrollCommands++");
		String[] commands = null;
		
		if (search == null || search.isEmpty()) {
			commands = allCommands;
		} else {
			commands = ldb.getValues(ldb.naturalLanguage(search, false, ' '), 0);
		}
		if (commands == null) {
			commands = new String[0];
		}
		
		int numCommands = Math.min(Math.max(0, commands.length - (page * 19)), 19);
		
		for (int i = 0; i < numCommands; i++) {
			if (i < 9) {
				retVal.setItem(i, getItemStack(permissionsPlayer, commands[page * 19 + i]));
			} else if (i >= 9 && i < 14) {
				retVal.setItem(i + 2, getItemStack(permissionsPlayer, commands[page * 19 + i]));
			} else {
				retVal.setItem(i + 6, getItemStack(permissionsPlayer, commands[page * 19 + i]));
			}
		}
		
		if (inventoryIsEmpty(retVal)) {
			retVal.setItem(13, getCloseItem());
		}
		
		if (commands.length > (page + 1) * 19) {
			retVal.setItem(26, getNextItem());
		}
		if (page != 0) {
			retVal.setItem(18, getPreviousItem());
		}
		
		return retVal;
	}
	
	//private
	private static ItemStack getItemStack(CommandSender sender, String command) {
		ItemStack item = null;
		
		if (guiRegistry.hasRegister(command)) {
			if (guiRegistry.hasRegister(command + "_data")) {
				item = new ItemStack((Material) guiRegistry.getRegister(command), 1, (short) guiRegistry.getRegister(command + "_data"));
			} else {
				item = new ItemStack((Material) guiRegistry.getRegister(command));
			}
		} else {
			item = new ItemStack(Material.STONE);
		}
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(((CommandUtil.hasPermission(sender, "tcpp.command." + command)) ? ChatColor.WHITE : ChatColor.RED) + "/" + command);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
		
		ArrayList<String> lore = new ArrayList<String>();
		if (!CommandUtil.hasPermission(sender, "tcpp.command." + command)) {
			lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "You do not have permissions");
			lore.add(ChatColor.RED + ChatColor.ITALIC.toString() + "to run this command!");
		}
		lore.addAll(splitDescription((String) commands.get(command).get("description")));
		lore.add(ChatColor.YELLOW + (String) commands.get(command).get("usage"));
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
				} else {
					currentRow += input.substring(0, space + 1);
					input = input.substring(space + 1);
				}
			}
			currentRow = currentRow.trim();
			if (!currentRow.isEmpty()) {
				retVal.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + currentRow);
			}
		}
		
		return retVal;
	}
}
