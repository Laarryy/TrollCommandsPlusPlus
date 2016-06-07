package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.ticks.DelayKillTickCommand;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.enums.CustomServiceType;
import ninja.egg82.plugin.utils.interfaces.ITickHandler;
import ninja.egg82.registry.interfaces.IRegistry;

public class DelayKillCommand extends BasePluginCommand {
	//vars
	ITickHandler tickHandler = (ITickHandler) ServiceLocator.getService(CustomServiceType.TICK_HANDLER);
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.DELAY_KILL_REGISTRY);
	
	//constructor
	public DelayKillCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_DELAYKILL, new int[]{1,2}, new int[]{0})) {
			if (args.length == 1) {
				e(Bukkit.getPlayer(args[0]), -1);
			} else if (args.length == 2) {
				try {
					e(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(MessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player, int delay) {
		String name = player.getName();
		
		if (reg.contains(name.toLowerCase())) {
			sender.sendMessage(name + "'s death is no longer inevitable.");
			reg.setRegister(name.toLowerCase(), null);
		} else {
			if (delay > -1) {
				sender.sendMessage(player.getName() + "'s death is inevitable.");
				reg.setRegister(name.toLowerCase(), ImmutableMap.of("time", System.currentTimeMillis(), "delay", delay, "player", player));
				tickHandler.addDelayedTickCommand("delayKill-" + player.getName().toLowerCase(), DelayKillTickCommand.class, 20 * delay + 2);
			}
		}
	}
}
