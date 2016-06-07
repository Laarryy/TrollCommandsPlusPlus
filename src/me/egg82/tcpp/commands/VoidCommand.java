package me.egg82.tcpp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.ticks.VoidTickCommand;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.CustomServiceType;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;

public class VoidCommand extends BasePluginCommand {
	//vars
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.VOID_REGISTRY);
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(CustomServiceType.TICK_HANDLER);
	
	//constructor
	public VoidCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_VOID, new int[]{1}, new int[]{0})) {
			if (args.length == 1) {
				e(Bukkit.getPlayer(args[0]));
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player) {
		ArrayList<Material[]> blocks = new ArrayList<Material[]>();
		Location loc = player.getLocation();
		
		blocks.add(removeBlocks(loc.clone().add(-1.0d, 0.0d, -1.0d)));
		blocks.add(removeBlocks(loc.clone().add(-1.0d, 0.0d, 0.0d)));
		blocks.add(removeBlocks(loc.clone().add(-1.0d, 0.0d, 1.0d)));
		blocks.add(removeBlocks(loc.clone().add(0.0d, 0.0d, -1.0d)));
		blocks.add(removeBlocks(loc.clone().add(0.0d, 0.0d, 0.0d)));
		blocks.add(removeBlocks(loc.clone().add(0.0d, 0.0d, 1.0d)));
		blocks.add(removeBlocks(loc.clone().add(1.0d, 0.0d, -1.0d)));
		blocks.add(removeBlocks(loc.clone().add(1.0d, 0.0d, 0.0d)));
		blocks.add(removeBlocks(loc.clone().add(1.0d, 0.0d, 1.0d)));
		
		reg.setRegister(player.getName().toLowerCase(), ImmutableMap.of("time", System.currentTimeMillis(), "loc", loc, "blocks", blocks));
		tickHandler.addDelayedTickCommand("void-" + player.getName().toLowerCase(), VoidTickCommand.class, 202);
		
		sender.sendMessage(player.getName() + " is now very confused as to why they are suddenly falling through the world.");
	}
	
	private Material[] removeBlocks(Location l) {
		Material[] b = new Material[l.getBlockY() + 1];
		int i = 0;
		Block block = null;
		
		do {
			block = l.getBlock();
			b[i] = block.getType();
			block.setType(Material.AIR);
			i++;
		} while (l.subtract(0.0d, 1.0d, 0.0d).getBlockY() >= 0);
		
		return b;
	}
}
