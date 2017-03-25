package me.egg82.tcpp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class SlowUndoCommand extends BasePluginCommand {
	//vars
	private IRegistry slowUndoRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOW_UNDO_REGISTRY);
	private IRegistry slowUndoInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOW_UNDO_INTERN_REGISTRY);
	
	//constructor
	public SlowUndoCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		slowUndoRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_SLOWUNDO, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (slowUndoRegistry.contains(uuid)) {
			sender.sendMessage("Any block changes " + player.getName() + " makes will no longer be slowly undone.");
			slowUndoRegistry.setRegister(uuid, null);
			slowUndoInternRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage("Any block changes " + player.getName() + " makes will now be slowly undone.");
			slowUndoRegistry.setRegister(uuid, player);
			slowUndoInternRegistry.setRegister(uuid, new ArrayList<ImmutableMap<String, Object>>());
		}
	}
}
