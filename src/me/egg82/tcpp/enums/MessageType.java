package me.egg82.tcpp.enums;

import org.bukkit.ChatColor;

public class MessageType {
	//vars
	public static final String PLAYER_IMMUNE = ChatColor.RED + "Player is immune.";
	public static final String NOT_CONTROLLING = ChatColor.RED + "You are not controlling anyone.";
	public static final String NO_CONTROL_SELF = ChatColor.RED + "You cannot control yourself!";
	public static final String NO_INSPECT_SELF = ChatColor.RED + "You cannot inspect yourself!";
	public static final String ALREADY_DISGUISED = ChatColor.RED + "You are already disguised as something else!";
	public static final String NO_LIBRARY = ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.";
	public static final String ALREADY_USED = ChatColor.RED + "This command is currently in use against this player. Please wait for it to complete before using it again.";
	public static final String NO_CHAT = ChatColor.RED + "You do not have permissions to chat while being controlled!";
	public static final String NOT_LIVING = ChatColor.RED + "The entity you have selected is neither a player nor a mob!";
	public static final String EMPOWERED = "The entity you have selected is now empowered!";
	public static final String DISEMPOWERED = "The entity you have selected is now disempowered!";
	public static final String WRONG_GAME_VERSION = ChatColor.RED + "This command has been disabled because this version of Minecraft doesn't support it.";
	public static final String MOB_NOT_FOUND = ChatColor.RED + "Mob not found.";
	public static final String POTION_NOT_FOUND = ChatColor.RED + "Potion effect not found.";
	public static final String COMMAND_NOT_FOUND = ChatColor.RED + "Command not found.";
	public static final String ENCHANT_NOT_FOUND = ChatColor.RED + "Enchantment not found.";
	public static final String VEGETABLE_NOT_FOUND = ChatColor.RED + "Vegetable not found.";
	
	//constructor
	public MessageType() {
		
	}
	
	//public
	
	//private
	
}