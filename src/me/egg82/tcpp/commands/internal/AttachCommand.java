package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.InvalidItemException;
import me.egg82.tcpp.exceptions.InvalidLibraryException;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.SenderNotAllowedException;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class AttachCommand extends PluginCommand {
	//vars
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	private ArrayList<String> commandNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AttachCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
		
		for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
			String name = topic.getName();
			if (name.charAt(0) != '/') {
				continue;
			}
			
			commandNames.add(name.substring(1, name.length()));
		}
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].isEmpty()) {
				return commandNames;
			} else {
				ArrayList<String> retVal = new ArrayList<String>();
				
				for (int i = 0; i < commandNames.size(); i++) {
					if (commandNames.get(i).toLowerCase().startsWith(args[0].toLowerCase())) {
						retVal.add(commandNames.get(i));
					}
				}
				
				return retVal;
			}
		} else if (args.length > 1) {
			org.bukkit.command.PluginCommand pluginCommand = Bukkit.getPluginCommand(args[0]);
			
			if (pluginCommand != null) {
				ArrayList<String> newArgs = new ArrayList<String>(Arrays.asList(args));
				newArgs.remove(0);
				return pluginCommand.tabComplete(sender, label, newArgs.toArray(new String[0]));
			}
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_ATTACH)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_ATTACH)));
			return;
		}
		if (!nbtHelper.isValidLibrary()) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_LIBRARY));
			onError().invoke(this, new ExceptionEventArgs<InvalidLibraryException>(new InvalidLibraryException(nbtHelper)));
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.SENDER_NOT_ALLOWED));
			onError().invoke(this, new ExceptionEventArgs<SenderNotAllowedException>(new SenderNotAllowedException(sender, this)));
			return;
		}
		
		ItemStack item = playerHelper.getItemInMainHand((Player) sender);
		
		if (item == null || item.getType() == Material.AIR) {
			sender.sendMessage(LanguageUtil.getString(LanguageType.INVALID_ITEM));
			onError().invoke(this, new ExceptionEventArgs<InvalidItemException>(new InvalidItemException(item)));
			return;
		}
		
		INBTCompound compound = nbtHelper.getCompound(item);
		
		if (args.length == 0) {
			if (compound.hasTag("tcppCommand")) {
				eUndo(item, compound);
				onComplete().invoke(this, CompleteEventArgs.EMPTY);
				return;
			} else {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
				String name = getClass().getSimpleName();
				name = name.substring(0, name.length() - 7).toLowerCase();
				sender.getServer().dispatchCommand(sender, "troll help " + name);
				onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
				return;
			}
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
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(ItemStack item, INBTCompound compound, String runnableCommand) {
		compound.setString("tcppSender", ((Player) sender).getUniqueId().toString());
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