package me.egg82.tcpp.util;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import ninja.egg82.utils.Util;

public class RegistryUtil {
	//vars
	private static String commandsPackage = "me.egg82.tcpp.commands";
	
	//constructor
	public RegistryUtil() {
		
	}
	
	//public
	public static void intialize() {
		
	}
	
	public static void onQuit(String name, Player player) {
		ArrayList<Class<? extends BasePluginCommand>> enums = Util.getClasses(BasePluginCommand.class, commandsPackage);
		for (Class<? extends BasePluginCommand> c : enums) {
			String pkg = c.getName();
			pkg = pkg.substring(0, pkg.lastIndexOf('.'));
			
			if (!pkg.equalsIgnoreCase(commandsPackage)) {
				continue;
			}
			
			BasePluginCommand run = null;
			
			try {
				run = c.getDeclaredConstructor(CommandSender.class, org.bukkit.command.Command.class, String.class, String[].class).newInstance(null, null, null, null);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				return;
			}
			
			run.onQuit(name, player);
		}
	}
	
	public static void onDeath(String name, Player player) {
		ArrayList<Class<? extends BasePluginCommand>> enums = Util.getClasses(BasePluginCommand.class, commandsPackage);
		for (Class<? extends BasePluginCommand> c : enums) {
			String pkg = c.getName();
			pkg = pkg.substring(0, pkg.lastIndexOf('.'));
			
			if (!pkg.equalsIgnoreCase(commandsPackage)) {
				continue;
			}
			
			BasePluginCommand run = null;
			
			try {
				run = c.getDeclaredConstructor(CommandSender.class, org.bukkit.command.Command.class, String.class, String[].class).newInstance(null, null, null, null);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				return;
			}
			
			run.onDeath(name, player);
		}
	}
	
	
	public static void onLogin(String name, Player player) {
		ArrayList<Class<? extends BasePluginCommand>> enums = Util.getClasses(BasePluginCommand.class, commandsPackage);
		for (Class<? extends BasePluginCommand> c : enums) {
			String pkg = c.getName();
			pkg = pkg.substring(0, pkg.lastIndexOf('.'));
			
			if (!pkg.equalsIgnoreCase(commandsPackage)) {
				continue;
			}
			
			BasePluginCommand run = null;
			
			try {
				run = c.getDeclaredConstructor(CommandSender.class, org.bukkit.command.Command.class, String.class, String[].class).newInstance(null, null, null, null);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				return;
			}
			
			run.onLogin(name, player);
		}
	}
	
	//private
	
}
