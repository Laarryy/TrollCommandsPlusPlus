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

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.CommandUtil;

public class AttachCommandCommand extends PluginCommand {
	//vars
	private IPlayerHelper playerHelper = (IPlayerHelper) ServiceLocator.getService(IPlayerHelper.class);
	private INBTHelper nbtHelper = (INBTHelper) ServiceLocator.getService(INBTHelper.class);
	private ArrayList<String> commandNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public AttachCommandCommand(CommandSender sender, Command command, String label, String[] args) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_ATTACH_COMMAND)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		if (!nbtHelper.isValidLibrary()) {
			sender.sendMessage(MessageType.NO_LIBRARY);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(SpigotMessageType.CONSOLE_NOT_ALLOWED);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.CONSOLE_NOT_ALLOWED);
			return;
		}
		
		ItemStack item = playerHelper.getItemInMainHand((Player) sender);
		
		String command = "";
		for (int i = 0; i < args.length; i++) {
			command += args[i] + " ";
		}
		command = command.trim();
		
		if (item != null && item.getType() != Material.AIR) {
			e(item, command);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(ItemStack item, String runnableCommand) {
		nbtHelper.addTag(item, "tcppSender", ((Player) sender).getUniqueId().toString());
		nbtHelper.addTag(item, "tcppCommand", runnableCommand);
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Command to run:");
		lore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "/" + runnableCommand);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage("\"/" + runnableCommand + "\" is now attached to this item!");
	}
	
	protected void onUndo() {
		
	}
}