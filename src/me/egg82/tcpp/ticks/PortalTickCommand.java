package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class PortalTickCommand extends TickCommand {
	//vars
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.PORTAL_REGISTRY);
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(SpigotServiceType.TICK_HANDLER);
	
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
		
		Location l = null;
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				l = loc.clone().add(i - 1.0d, 0.0d, j - 1.0d);
				
				BlockUtil.addYLineBlocks(l.clone(), blocks.get(MathUtil.toXY(3, j, i)));
				BlockUtil.setYLineBlockState(l.clone(), data.get(MathUtil.toXY(3, j, i)));
				BlockUtil.setYLineBlockInventory(l.clone(), inv.get(MathUtil.toXY(3, j, i)));
			}
		}
		
		reg.setRegister(name, null);
	}
}
