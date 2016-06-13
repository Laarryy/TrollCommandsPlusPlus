package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.registry.interfaces.IRegistry;

public class DisplayCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.DISPLAY_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.DISPLAY_INTERN_REGISTRY);
	
	//constructor
	public DisplayCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	@SuppressWarnings("unchecked")
	public void onQuit(String name, Player player) {
		reg.setRegister(name, null);
		
		if (reg2.contains(name)) {
			HashMap<String, Object> map = (HashMap<String, Object>) reg2.getRegister(name);
			reg2.setRegister(name, null);
			set((Location) map.get("loc"), (boolean) map.get("ground"), Material.AIR, Material.AIR);
		}
	}
	public void onDeath(String name, Player player) {
		onQuit(name, player);
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_DISPLAY, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getName(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String name, Player player) {
		Location loc = null;
		HashMap<String, Object> map = null;
		boolean onGround;
		
		if (reg.contains(name.toLowerCase())) {
			sender.sendMessage(name + " is no longer on display.");
			
			map = (HashMap<String, Object>) reg2.getRegister(name.toLowerCase());
			
			reg.setRegister(name.toLowerCase(), null);
			reg2.setRegister(name.toLowerCase(), null);
			
			set((Location) map.get("loc"), (boolean) map.get("ground"), Material.AIR, Material.AIR);
		} else {
			sender.sendMessage(name + " is now on display.");
			
			map = new HashMap<String, Object>();
			loc = player.getLocation().clone();
			loc.setX(loc.getBlockX() + 0.5d);
			loc.setY(loc.getBlockY());
			loc.setZ(loc.getBlockZ() + 0.5d);
			player.teleport(loc);
			onGround = (BlockUtil.getTopAirBlock(loc).getBlockY() == loc.getBlockY()) ? true : false;
			
			map.put("loc", loc);
			map.put("ground", onGround);
			
			set(loc, onGround, Material.THIN_GLASS, Material.GLASS);
			
			reg.setRegister(name.toLowerCase(), player);
			reg2.setRegister(name.toLowerCase(), map);
		}
	}
	
	private void set(Location loc, boolean onGround, Material m1, Material m2) {
		if (!onGround) {
			loc.add(0.0d, -1.0d, 0.0d);
			loc.getBlock().setType(m2);
		}
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i == 1 && j == 1) {
					continue;
				}
				setMat((onGround) ? 2 : 3, loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), m1);
			}
		}
		loc.clone().add(0.0d, (onGround) ? 2.0d : 3.0d, 0.0d).getBlock().setType(m2);
		
		if (!onGround) {
			loc.add(0.0d, 1.0d, 0.0d);
		}
	}
	private void setMat(int height, Location l, Material m) {
		int endY = l.getBlockY() + height;
		
		do {
			l.getBlock().setType(m);
		} while (l.add(0.0d, 1.0d, 0.0d).getBlockY() <= endY);
	}
}
