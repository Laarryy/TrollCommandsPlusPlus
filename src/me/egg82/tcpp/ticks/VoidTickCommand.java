package me.egg82.tcpp.ticks;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;
import com.egg82.utils.MathUtil;
import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.PluginServiceType;

public class VoidTickCommand extends Command {
	//vars
	IRegistry voidRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VOID_REGISTRY);
	
	//constructor
	public VoidTickCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		String[] names = voidRegistry.registryNames();
		for (String name : names) {
			v((ImmutableMap<String, Object>) voidRegistry.getRegister(name));
		}
	}
	@SuppressWarnings("unchecked")
	private void v(ImmutableMap<String, Object> m) {
		long timePassed = System.currentTimeMillis() - (long) m.get("time");
		
		if (timePassed < 10000) {
			return;
		}
		
		Location loc = (Location) m.get("loc");
		ArrayList<Material[]> blocks = (ArrayList<Material[]>) m.get("blocks");
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addBlocks(loc.clone().add(i - 1.0d, 0.0d, j - 1.0d), blocks.get(MathUtil.toXY(3, j, i)));
			}
		}
	}
	
	private void addBlocks(Location l, Material[] blocks) {
		int i = 0;
		
		do {
			l.getBlock().setType(blocks[i]);
			i++;
		} while (l.subtract(0.0d, 1.0d, 0.0d).getBlockY() > 0);
	}
}
