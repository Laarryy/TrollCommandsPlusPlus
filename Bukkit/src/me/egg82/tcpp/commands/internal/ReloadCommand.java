package me.egg82.tcpp.commands.internal;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.egg82.tcpp.Loaders;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.bukkit.services.ConfigRegistry;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.YamlUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.utils.FileUtil;

public class ReloadCommand extends CommandHandler {
	//vars
	
	//constructor
	public ReloadCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_RELOAD)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		// Config
		ServiceLocator.getService(ConfigRegistry.class).load(YamlUtil.getOrLoadDefaults(ServiceLocator.getService(Plugin.class).getDataFolder().getAbsolutePath() + FileUtil.DIRECTORY_SEPARATOR_CHAR + "config.yml", "config.yml", true));
		
		// Messaging
		List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
		for (IMessageHandler handler : services) {
			try {
				handler.close();
			} catch (Exception ex) {
				
			}
		}
		
		Loaders.loadMessaging();
		
		if (ServiceLocator.hasService(IMessageHandler.class)) {
			ServiceLocator.getService(IMessageHandler.class).addHandlersFromPackage("me.egg82.tcpp.messages");
		}
	}
	protected void onUndo() {
		
	}
}
