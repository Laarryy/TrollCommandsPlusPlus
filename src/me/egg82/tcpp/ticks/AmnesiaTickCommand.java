package me.egg82.tcpp.ticks;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class AmnesiaTickCommand extends TickCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.AMNESIA_INTERN_REGISTRY);
	
	//constructor
	public AmnesiaTickCommand() {
		super();
		ticks = 20l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = reg2.registryNames();
		for (String name : names) {
			e((Player) reg.getRegister(name));
		}
	}
	@SuppressWarnings("unchecked")
	private void e(Player player) {
		if(player == null) {
			return;
		}
		
		ArrayList<ImmutableMap<String, Object>> maps = (ArrayList<ImmutableMap<String, Object>>) reg2.getRegister(player.getName().toLowerCase());
		ArrayList<ImmutableMap<String, Object>> rem = new ArrayList<ImmutableMap<String, Object>>();
		
		for (ImmutableMap<String, Object> map : maps) {
			if (Math.random() <= 0.1d) {
				try {
					player.sendMessage(String.format((String) map.get("format"), player.getDisplayName(), (String) map.get("message")));
				} catch (Exception ex) {
		        	
		        }
				
				rem.add(map);
			}
		}
		
		maps.removeAll(rem);
	}
}