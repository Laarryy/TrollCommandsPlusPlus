package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.TrollInventoryRegistry;
import me.egg82.tcpp.registries.TrollPageRegistry;
import me.egg82.tcpp.registries.TrollPlayerRegistry;
import me.egg82.tcpp.registries.TrollSearchRegistry;
import me.egg82.tcpp.util.GuiUtil;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class SearchCommand extends CommandHandler {
	//vars
	private IVariableRegistry<UUID> trollInventoryRegistry = ServiceLocator.getService(TrollInventoryRegistry.class);
	private IVariableRegistry<UUID> trollPlayerRegistry = ServiceLocator.getService(TrollPlayerRegistry.class);
	private IVariableRegistry<UUID> trollPageRegistry = ServiceLocator.getService(TrollPageRegistry.class);
	private IVariableRegistry<UUID> trollSearchRegistry = ServiceLocator.getService(TrollSearchRegistry.class);
	
	private ArrayList<String> commandNames = new ArrayList<String>();
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public SearchCommand() {
		super();
		
		String[] list = ((String) ServiceLocator.getService(JavaPlugin.class).getDescription().getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
		for (String entry : list) {
			if (entry.contains("-= Available Commands =-")) {
				continue;
			}
			
			commandNames.add(entry.substring(0, entry.indexOf(':')).trim().split(" ")[1]);
		}
	}
	
	//public
	public List<String> tabComplete() {
		if (args.length == 1) {
			ArrayList<String> retVal = new ArrayList<String>();
			
			if (args[0].isEmpty()) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					retVal.add(player.getName());
				}
			} else {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						retVal.add(player.getName());
					}
				}
			}
			
			return retVal;
		}
		
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_SEARCH)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		if (sender.isConsole()) {
			sender.sendMessage(ChatColor.RED + "Console cannot run this command!");
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Player could not be found.");
			return;
		}
		if (player.hasPermission(PermissionsType.IMMUNE)) {
			sender.sendMessage(ChatColor.RED + "Player is immune.");
			return;
		}
		
		String search = "";
		if (args.length >= 2) {
			for (int i = 1; i < args.length; i++) {
				search += args[i] + " ";
			}
			search = search.trim();
		}
		
		e(player, sender.getUuid(), (Player) sender.getHandle(), search);
	}
	private void e(Player player, UUID senderUuid, Player senderPlayer, String search) {
		Inventory inv = GuiUtil.createInventory(senderPlayer, search, 0);
		trollInventoryRegistry.setRegister(senderUuid, inv);
		trollPlayerRegistry.setRegister(senderUuid, player.getName());
		trollPageRegistry.setRegister(senderUuid, 0);
		trollSearchRegistry.setRegister(senderUuid, search);
		
		senderPlayer.openInventory(inv);
		
		metricsHelper.commandWasRun(this);
	}
	
	protected void onUndo() {
		
	}
}