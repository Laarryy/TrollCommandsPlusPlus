package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
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

public class AttachCommand extends CommandHandler {
	//vars
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	private ArrayList<String> commandNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AttachCommand() {
		super();
		
		for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
			String name = topic.getName();
			if (name.charAt(0) != '/') {
				continue;
			}
			
			commandNames.add(name.substring(1, name.length()));
		}
	}
	
	//public
	public List<String> tabComplete() {
		if (args.length == 1) {
			if (args[0].isEmpty()) {
				return commandNames;
			}
			
			ArrayList<String> retVal = new ArrayList<String>();
			
			for (int i = 0; i < commandNames.size(); i++) {
				if (commandNames.get(i).toLowerCase().startsWith(args[0].toLowerCase())) {
					retVal.add(commandNames.get(i));
				}
			}
			
			return retVal;
		} else if (args.length > 1) {
			org.bukkit.command.PluginCommand pluginCommand = Bukkit.getPluginCommand(args[0]);
			
			if (pluginCommand != null) {
				ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
				newArgs.remove(0);
				return pluginCommand.tabComplete((CommandSender) sender.getHandle(), "", newArgs.toArray(new String[0]));
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_ATTACH)) {
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
		
		ItemStack item = playerHelper.getItemInMainHand((Player) sender.getHandle());
		
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(ChatColor.RED + "Item is invalid.");
			return;
		}
		
		INBTCompound compound = nbtHelper.getCompound(item);
		
		if (args.length == 0) {
			if (compound.hasTag("tcppCommand")) {
				eUndo(item, compound);
				return;
			}
			
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		String command = "";
		for (int i = 0; i < args.length; i++) {
			command += args[i] + " ";
		}
		command = command.trim();
		
		if (!compound.hasTag("tcppCommand")) {
			e(item, compound, command);
		} else {
			eUndo(item, compound);
		}
	}
	private void e(ItemStack item, INBTCompound compound, String runnableCommand) {
		compound.setString("tcppSender", sender.getUuid().toString());
		compound.setString("tcppCommand", runnableCommand);
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		if (meta.hasLore()) {
			lore.addAll(meta.getLore());
		}
		lore.add("Command to run:");
		lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "/" + runnableCommand);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("\"/" + runnableCommand + "\" is now attached to this item!");
	}
	
	protected void onUndo() {
		
	}
	private void eUndo(ItemStack item, INBTCompound compound) {
		compound.removeTag("tcppSender");
		compound.removeTag("tcppCommand");
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>(meta.getLore());
		int removeLine = -1;
		for (int i = 0; i < lore.size(); i++) {
			if (lore.get(i).contains("Command to run:")) {
				removeLine = i;
				break;
			}
		}
		if (removeLine > -1) {
			lore.remove(removeLine);
			lore.remove(removeLine);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		sender.sendMessage("There is no longer a command attached to this item.");
	}
}