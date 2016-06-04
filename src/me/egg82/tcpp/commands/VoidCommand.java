package me.egg82.tcpp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.commands.PluginCommand;
import com.egg82.plugin.enums.CustomServiceType;
import com.egg82.plugin.utils.interfaces.ITickHandler;
import com.egg82.registry.interfaces.IRegistry;
import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.ticks.VoidTickCommand;

public class VoidCommand extends PluginCommand {
	//vars
	IRegistry voidRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VOID_REGISTRY);
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(CustomServiceType.TICK_HANDLER);
	
	//constructor
	public VoidCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_VOID)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 1) {
			v(Bukkit.getPlayer(args[0]));
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	private void v(Player player) {
		if (player == null) {
			sender.sendMessage(MessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
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
		
		voidRegistry.setRegister(player.getName(), ImmutableMap.of("time", System.currentTimeMillis(), "loc", loc, "blocks", blocks));
		tickHandler.addDelayedTickCommand("void-" + player.getName(), VoidTickCommand.class, 202);
		
		sender.sendMessage(player.getName() + " is now very confused as to why they are suddenly falling through the world.");
		
		dispatch(CommandEvent.COMPLETE, null);
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
