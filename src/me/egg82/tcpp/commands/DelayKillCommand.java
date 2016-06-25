package me.egg82.tcpp.commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.ticks.DelayKillTickCommand;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;

public class DelayKillCommand extends BasePluginCommand {
	//vars
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(SpigotServiceType.TICK_HANDLER);
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.DELAY_KILL_REGISTRY);
	
	//constructor
	public DelayKillCommand() {
		super();
	}
	
	//public
	public void onQuit(String name, Player player) {
		reg.computeIfPresent(name, (k,v) -> {
			return null;
		});
	}
	public void onDeath(String name, Player player) {
		onQuit(name, player);
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_DELAYKILL, new int[]{1,2}, new int[]{0})) {
			if (args.length == 1) {
				Player player = Bukkit.getPlayer(args[0]);
				e(player.getName(), player, -1);
			} else if (args.length == 2) {
				Player player = Bukkit.getPlayer(args[0]);
				
				try {
					e(player.getName(), player, Integer.parseInt(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String name, Player player, int delay) {
		String lowerName = name.toLowerCase();
		
		if (reg.contains(lowerName)) {
			sender.sendMessage(name + "'s death is no longer inevitable.");
			reg.setRegister(lowerName, null);
		} else {
			if (delay > -1) {
				sender.sendMessage(name + "'s death is inevitable.");
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("time", System.currentTimeMillis());
				map.put("delay", delay);
				map.put("player", player);
				reg.setRegister(lowerName, map);
				tickHandler.addDelayedTickCommand("delayKill-" + lowerName, DelayKillTickCommand.class, 20 * delay + 2);
			}
		}
	}
}
