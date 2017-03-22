package me.egg82.tcpp.commands;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.util.interfaces.IDisguiseHelper;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;

public class ScareCommand extends BasePluginCommand {
	//vars
	private IDisguiseHelper disguiseHelper = (IDisguiseHelper) ServiceLocator.getService(PluginServiceType.DISGUISE_HELPER);
	
	//constructor
	public ScareCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(true, PermissionsType.COMMAND_SCARE, new int[]{0}, null)) {
			if (!disguiseHelper.isValidLibrary()) {
				sender.sendMessage(MessageType.NO_LIBRARY);
				dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
				return;
			}
			
			Player player = (Player) sender;
			EntityType type = disguiseHelper.disguiseType(player);
			
			if (type == null) {
				disguiseHelper.disguiseAsEntity(player, EntityType.CREEPER);
				sender.sendMessage("You are now a creeper. Ooh, scary!");
			} else if (type == EntityType.CREEPER) {
				disguiseHelper.undisguise(player);
				sender.sendMessage("You are no longer a creeper.");
			} else {
				sender.sendMessage(MessageType.ALREADY_DISGUISED);
				dispatch(CommandEvent.ERROR, CommandErrorType.ALREADY_DISGUISED);
				return;
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
}
