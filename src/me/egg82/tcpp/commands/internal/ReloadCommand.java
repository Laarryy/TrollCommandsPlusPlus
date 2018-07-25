package me.egg82.tcpp.commands.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.egg82.tcpp.Configuration;
import me.egg82.tcpp.Loaders;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.bukkit.BasePlugin;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.SenderType;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.utils.DirectoryUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

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
		File configFile = new File(ServiceLocator.getService(Plugin.class).getDataFolder(), "config.yml");
		if (configFile.exists() && configFile.isDirectory()) {
			DirectoryUtil.delete(configFile);
		}
		if (!configFile.exists()) {
			try (InputStreamReader reader = new InputStreamReader(ServiceLocator.getService(Plugin.class).getResource("config.yml")); BufferedReader in = new BufferedReader(reader); FileWriter writer = new FileWriter(configFile); BufferedWriter out = new BufferedWriter(writer)) {
				while (in.ready()) {
					writer.write(in.readLine());
				}
			} catch (Exception ex) {
				
			}
		}
		
		ConfigurationLoader<ConfigurationNode> loader = YAMLConfigurationLoader.builder().setIndent(2).setFile(configFile).build();
		ConfigurationNode root = null;
		try {
			root = loader.load();
		} catch (Exception ex) {
			throw new RuntimeException("Error loading config. Aborting plugin load.", ex);
		}
		Configuration config = new Configuration(root);
		ServiceLocator.removeServices(Configuration.class);
		ServiceLocator.provideService(config);
		
		// Messaging
		List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
		for (IMessageHandler handler : services) {
			try {
				handler.close();
			} catch (Exception ex) {
				
			}
		}
		
		Loaders.loadMessaging(ServiceLocator.getService(Plugin.class).getDescription().getName(), Bukkit.getServerName(), ServiceLocator.getService(BasePlugin.class).getServerId(), SenderType.SERVER);
		
		if (ServiceLocator.hasService(IMessageHandler.class)) {
			ServiceLocator.getService(IMessageHandler.class).addHandlersFromPackage("me.egg82.tcpp.messages");
		}
	}
	protected void onUndo() {
		
	}
}
