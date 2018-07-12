package me.egg82.tcpp.commands.internal;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.registries.EmpowerRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;

public class EmpowerCommand extends CommandHandler {
	//vars
	private IVariableRegistry<UUID> empowerRegistry = ServiceLocator.getService(EmpowerRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public EmpowerCommand() {
		super();
	}
	
	//public
	public List<String> tabComplete() {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!sender.hasPermission(PermissionsType.COMMAND_EMPOWER)) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
			return;
		}
		if (!CommandUtil.isPlayer((CommandSender) sender.getHandle())) {
			sender.sendMessage(ChatColor.RED + "Console cannot run this command!");
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0)) {
			sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
			return;
		}
		
		e(sender.getUuid());
	}
	private void e(UUID uuid) {
		if (empowerRegistry.hasRegister(uuid)) {
			empowerRegistry.removeRegister(uuid);
			
			sender.sendMessage("You will no longer empower (or disempower) the next player or mob you right-click.");
		} else {
			empowerRegistry.setRegister(uuid, null);
			metricsHelper.commandWasRun(this);
			
			sender.sendMessage("You will now empower (or disempower) the next player or mob you right-click!");
		}
	}
	
	protected void onUndo() {
		
	}
}