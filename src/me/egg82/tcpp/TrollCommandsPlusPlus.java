package me.egg82.tcpp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.ControlHelper;
import me.egg82.tcpp.util.DisguiseHelper;
import me.egg82.tcpp.util.DisplayHelper;
import me.egg82.tcpp.util.LibsDisguisesHelper;
import me.egg82.tcpp.util.NullDisguiseHelper;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;
import net.gravitydevelopment.updater.Updater.UpdateType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.plugin.utils.SpigotReflectUtil;
import ninja.egg82.plugin.utils.VersionUtil;
import ninja.egg82.startup.InitRegistry;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private Timer updateTimer = null;
	
	private int numCommands = 0;
	private int numEvents = 0;
	private int numPermissions = 0;
	private int numTicks = 0;
	
	//constructor
	public TrollCommandsPlusPlus() {
		
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		SpigotReflectUtil.addServicesFromPackage("me.egg82.tcpp.services");
		
		PluginManager manager = getServer().getPluginManager();
		
		if (manager.getPlugin("LibsDisguises") != null) {
			if (manager.getPlugin("ProtocolLib") != null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[TrollCommands++] Enabling support for LibsDisguises.");
				ServiceLocator.provideService(LibsDisguisesHelper.class);
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TrollCommands++] LibsDisguises requires ProtocolLib to function, which was not found. The /control and /scare commands have been disabled.");
				ServiceLocator.provideService(NullDisguiseHelper.class);
			}
		} else if (manager.getPlugin("iDisguise") != null) {
			if (manager.getPlugin("ProtocolLib") != null) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[TrollCommands++] Enabling support for iDisguise.");
				ServiceLocator.provideService(DisguiseHelper.class);
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TrollCommands++] iDisguise requires ProtocolLib to function, which was not found. The /control and /scare commands have been disabled.");
				ServiceLocator.provideService(NullDisguiseHelper.class);
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[TrollCommands++] Neither LibsDisguises nor iDisguise was found. The /control and /scare commands have been disabled.");
			ServiceLocator.provideService(NullDisguiseHelper.class);
		}
		
		ServiceLocator.provideService(ControlHelper.class);
		ServiceLocator.provideService(DisplayHelper.class);
		
		updateTimer = new Timer(24 * 60 * 60 * 1000, onUpdateTimer);
	}
	
	public void onEnable() {
		super.onEnable();
		
		try {
			@SuppressWarnings("unused")
			Metrics m = new Metrics(this);
		} catch (Exception ex) {
			
		}
		
		numCommands = SpigotReflectUtil.addCommandsFromPackage("me.egg82.tcpp.commands");
		numEvents = SpigotReflectUtil.addEventsFromPackage("me.egg82.tcpp.events");
		numPermissions = SpigotReflectUtil.addPermissionsFromClass(PermissionsType.class);
		numTicks = SpigotReflectUtil.addTicksFromPackage("me.egg82.tcpp.ticks");
		
		enableMessage(Bukkit.getConsoleSender());
		checkUpdate();
		updateTimer.setRepeats(true);
		updateTimer.start();
	}
	public void onDisable() {
		super.onDisable();
		
		SpigotReflectUtil.clearAll();
		disableMessage(Bukkit.getConsoleSender());
	}
	
	//private
	private ActionListener onUpdateTimer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			checkUpdate();
		}
	};
	private void checkUpdate() {
		Updater updater = new Updater(this, 100359, getFile(), UpdateType.NO_DOWNLOAD, false);
		if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
			String latestVersion = updater.getLatestName();
			latestVersion = latestVersion.substring(latestVersion.lastIndexOf('v') + 1);
			String currentVersion = getDescription().getVersion();
			
			int[] latest = VersionUtil.parseVersion(latestVersion, '.');
			int[] current = VersionUtil.parseVersion(currentVersion, '.');
			
			for (int i = 0; i < Math.min(latest.length, current.length); i++) {
				if (latest[i] < current[i]) {
					return;
				}
			}
			
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--== " + ChatColor.YELLOW + "TrollCommands++ UPDATE AVAILABLE (Latest: " + latestVersion + " Current: " + currentVersion + ") " + ChatColor.GREEN + " ==--");
		}
	}
	
	private void enableMessage(ConsoleCommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "  _______        _ _  _____                                          _                 ");
		sender.sendMessage(ChatColor.AQUA + " |__   __|      | | |/ ____|                                        | |      _     _   ");
		sender.sendMessage(ChatColor.AQUA + "    | |_ __ ___ | | | |     ___  _ __ ___  _ __ ___   __ _ _ __   __| |___ _| |_ _| |_ ");
		sender.sendMessage(ChatColor.AQUA + "    | | '__/ _ \\| | | |    / _ \\| '_ ` _ \\| '_ ` _ \\ / _` | '_ \\ / _` / __|_   _|_   _|");
		sender.sendMessage(ChatColor.AQUA + "    | | | | (_) | | | |___| (_) | | | | | | | | | | | (_| | | | | (_| \\__ \\ |_|   |_|  ");
		sender.sendMessage(ChatColor.AQUA + "    |_|_|  \\___/|_|_|\\_____\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|\\__,_|___/            ");
		sender.sendMessage(ChatColor.GREEN + "[Version " + getDescription().getVersion() + "] " + ChatColor.RED + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.WHITE + numPermissions + " permissions " + ChatColor.YELLOW + numTicks + " tick handlers");
		sender.sendMessage(ChatColor.WHITE + "[TrollCommands++] " + ChatColor.GRAY + "Attempting to load compatibility with Bukkit version " + ((InitRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("game.version"));
	}
	private void disableMessage(ConsoleCommandSender sender) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Disabled" + ChatColor.GREEN + " ==--");
	}
}