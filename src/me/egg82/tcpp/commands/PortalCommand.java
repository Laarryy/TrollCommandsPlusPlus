package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.HotTubRegistry;
import me.egg82.tcpp.services.PortalRegistry;
import me.egg82.tcpp.services.VoidRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import me.egg82.tcpp.util.WorldHoleHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;

public class PortalCommand extends PluginCommand {
	//vars
	private IRegistry portalRegistry = (IRegistry) ServiceLocator.getService(PortalRegistry.class);
	private IRegistry voidRegistry = (IRegistry) ServiceLocator.getService(VoidRegistry.class);
	private IRegistry hotTubRegistry = (IRegistry) ServiceLocator.getService(HotTubRegistry.class);
	
	private WorldHoleHelper worldHoleHelper = (WorldHoleHelper) ServiceLocator.getService(WorldHoleHelper.class);
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public PortalCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_PORTAL)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		String uuid = player.getUniqueId().toString();
		
		if (portalRegistry.hasRegister(uuid)) {
			sender.sendMessage(MessageType.ALREADY_USED);
			dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
			return;
		}
		if (voidRegistry.hasRegister(uuid)) {
			sender.sendMessage(MessageType.ALREADY_USED);
			dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
			return;
		}
		if (hotTubRegistry.hasRegister(uuid)) {
			sender.sendMessage(MessageType.ALREADY_USED);
			dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_USED);
			return;
		}
		
		e(uuid, player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		worldHoleHelper.portalHole(uuid, player);
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage(player.getName() + " is now falling to The(ir) End.");
	}
}
