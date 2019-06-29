package me.egg82.tcpp.utils;

import org.bukkit.ChatColor;

public class LogUtil {
    private LogUtil() {}

    public static String getHeading() { return ChatColor.YELLOW + "[" + ChatColor.AQUA + "TrollCommands++" + ChatColor.YELLOW + "] " + ChatColor.RESET; }

    public static String getSourceHeading(String source) { return ChatColor.YELLOW + "[" + ChatColor.LIGHT_PURPLE + source + ChatColor.YELLOW + "] " + ChatColor.RESET; }
}
