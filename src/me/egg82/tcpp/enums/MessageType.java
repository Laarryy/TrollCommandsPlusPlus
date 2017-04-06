package me.egg82.tcpp.enums;

import org.bukkit.ChatColor;

public class MessageType {
	//vars
	public static final String PLAYER_IMMUNE = ChatColor.RED + "Player is immune.";
	public static final String NOT_CONTROLLING = ChatColor.RED + "You are not controlling anyone.";
	public static final String NO_CONTROL_SELF = ChatColor.RED + "You cannot control yourself!";
	public static final String ALREADY_DISGUISED = ChatColor.RED + "You are already disguised as something else!";
	public static final String NO_LIBRARY = ChatColor.RED + "This command has been disabled because there is no recognized backing library available. Please install one and restart the server to enable this command.";
	public static final String ALREADY_USED = ChatColor.RED + "This command is currently in use against this player. Please wait for it to finish.";
	public static final String NO_CHAT = ChatColor.RED + "You do not have permissions to chat while being controlled!";
	
	//constructor
	public MessageType() {
		
	}
	
	//public
	
	//private
	
}