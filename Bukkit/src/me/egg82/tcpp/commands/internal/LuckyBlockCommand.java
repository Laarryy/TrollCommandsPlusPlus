package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.reflection.player.IPlayerHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.MathUtil;

public class LuckyBlockCommand extends CommandHandler {
	//vars
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public LuckyBlockCommand() {
		super();
	}
	
	//public
	public List<String> tabComplete() {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_LUCKY_BLOCK)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!nbtHelper.isValidLibrary()) {
			sender.sendMessage(ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.");
			return;
		}
		if (!CommandUtil.isPlayer((CommandSender) sender.getHandle())) {
			sender.sendMessage(ChatColor.RED + "Console cannot run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0, 1)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		ItemStack item = playerHelper.getItemInMainHand((Player) sender.getHandle());
		
		if (item == null || item.getType() == Material.AIR || !item.getType().isBlock()) {
			sender.sendMessage(ChatColor.RED + "Item is invalid.");
			return;
		}
		
		INBTCompound compound = nbtHelper.getCompound(item);
		double luckyChance = 0.5;
		
		if (args.length == 1) {
			try {
				luckyChance = Double.parseDouble(args[0]) / 100.0d;
			} catch (Exception ex) {
				sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
				return;
			}
			
			luckyChance = MathUtil.clamp(0.0d, 1.0d, luckyChance);
		}
		
		if (!compound.hasTag("tcppLucky")) {
			e(item, compound, luckyChance);
		} else {
			eUndo(item, compound);
		}
	}
	private void e(ItemStack item, INBTCompound compound, double luckyChance) {
		compound.setDouble("tcppLucky", luckyChance);
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new ArrayList<String>();
		}
		lore.add(0, ChatColor.GOLD + ChatColor.BOLD.toString() + "LUCKY?");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("This item is now a lucky block!");
	}
	
	protected void onUndo() {
		
	}
	private void eUndo(ItemStack item, INBTCompound compound) {
		compound.removeTag("tcppLucky");
		
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore != null) {
			if (lore.get(0).contains("LUCKY?")) {
				lore.remove(0);
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		sender.sendMessage("This item is no longer a lucky block.");
	}
}