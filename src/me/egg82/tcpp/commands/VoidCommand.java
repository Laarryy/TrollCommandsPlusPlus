package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.ticks.VoidTickCommand;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;

public class VoidCommand extends BasePluginCommand {
	//vars
	IRegistry voidRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VOID_REGISTRY);
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(SpigotServiceType.TICK_HANDLER);
	
	//constructor
	public VoidCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_VOID, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			String uuid = player.getUniqueId().toString();
			
			if (voidRegistry.contains(uuid)) {
				sender.sendMessage(MessageType.ALREADY_USED);
				dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
				return;
			}
			
			e(uuid, player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		ArrayList<Material[]> blocks = new ArrayList<Material[]>();
		ArrayList<ArrayList<ItemStack[]>> inv = new ArrayList<ArrayList<ItemStack[]>>();
		ArrayList<BlockState[]> data = new ArrayList<BlockState[]>();
		Location loc = player.getLocation();
		
		Location l = null;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				l = loc.clone().add(i - 1.0d, 0.0d, j - 1.0d);
				
				inv.add(BlockUtil.getYLineBlockInventory(l.clone(), 0));
				data.add(BlockUtil.getYLineBlockState(l.clone(), 0));
				blocks.add(BlockUtil.removeYLineBlocks(l.clone(), 0));
			}
		}
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("time", System.currentTimeMillis());
		map.put("loc", loc);
		map.put("blocks", blocks);
		map.put("inv", inv);
		map.put("data", data);
		voidRegistry.setRegister(uuid, map);
		tickHandler.addDelayedTickCommand("void-" + uuid, VoidTickCommand.class, 202);
		
		sender.sendMessage(player.getName() + " is now very confused as to why they are suddenly falling through the world.");
	}
}
