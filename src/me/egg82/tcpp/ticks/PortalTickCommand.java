package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class PortalTickCommand extends Command {
	//vars
	IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.PORTAL_REGISTRY);
	
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
			return;
		}
		
		Location loc = (Location) m.get("loc");
		ArrayList<Material[]> blocks = (ArrayList<Material[]>) m.get("blocks");
		//ArrayList<BlockState[]> data = (ArrayList<BlockState[]>) m.get("data");
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addBlocks(loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), blocks.get(MathUtil.toXY(3, j, i)));
				//setBlockState(loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), data.get(MathUtil.toXY(3, j, i)));
			}
		}
		
		reg.setRegister(name, null);
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
