package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.FlowerPot;
import org.bukkit.block.Jukebox;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.util.BlockDataUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.plugin.enums.CustomServiceType;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class PortalTickCommand extends Command {
	//vars
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.PORTAL_REGISTRY);
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(CustomServiceType.TICK_HANDLER);
	
	//constructor
	public PortalTickCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		String[] names = reg.registryNames();
		for (String name : names) {
			e(name, (HashMap<String, Object>) reg.getRegister(name));
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String name, HashMap<String, Object> m) {
		long timePassed = System.currentTimeMillis() - (long) m.get("time");
		
		if (timePassed < 5000) {
			tickHandler.addDelayedTickCommand(name, PortalTickCommand.class, 20);
			return;
		}
		
		Location loc = (Location) m.get("loc");
		ArrayList<Material[]> blocks = (ArrayList<Material[]>) m.get("blocks");
		ArrayList<BlockState[]> data = (ArrayList<BlockState[]>) m.get("data");
		ArrayList<ArrayList<ItemStack[]>> inv = (ArrayList<ArrayList<ItemStack[]>>) m.get("inv");
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addBlocks(loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), blocks.get(MathUtil.toXY(3, j, i)));
				setBlockState(loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), data.get(MathUtil.toXY(3, j, i)));
				setBlockInventory(loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), inv.get(MathUtil.toXY(3, j, i)));
			}
		}
		
		reg.setRegister(name, null);
	}
	
	private void setBlockInventory(Location l, ArrayList<ItemStack[]> inv) {
		BlockState state = null;
		Material type = null;
		int i = 0;
		ItemStack[] stack = null;
		
		do {
			state = l.getBlock().getState();
			type = state.getType();
			if (state instanceof InventoryHolder) {
				InventoryHolder holder = (InventoryHolder) state;
				holder.getInventory().setContents(inv.get(i));
			} else if (type == Material.FLOWER_POT) {
				stack = inv.get(i);
				if (stack != null) {
					((FlowerPot) state).setContents(stack[0].getData());
				}
			} else if (type == Material.JUKEBOX) {
				stack = inv.get(i);
				if (stack != null) {
					((Jukebox) state).setPlaying(stack[0].getType());
				}
			}
			i++;
		} while (l.subtract(0.0d, 1.0d, 0.0d).getBlockY() >= 0);
	}
	private void setBlockState(Location l, BlockState[] data) {
		int i = 0;
		int endY = l.getBlockY() - 5;
		
		do {
			BlockDataUtil.setBlockData(l.getBlock().getState(), data[i]);
			i++;
		} while (l.subtract(0.0d, 1.0d, 0.0d).getBlockY() >= endY);
	}
	private void addBlocks(Location l, Material[] blocks) {
		int i = 0;
		int endY = l.getBlockY() - 5;
		
		do {
			l.getBlock().setType(blocks[i]);
			i++;
		} while (l.subtract(0.0d, 1.0d, 0.0d).getBlockY() >= endY);
	}
}
