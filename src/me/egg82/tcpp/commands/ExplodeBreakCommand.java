package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class ExplodeBreakCommand extends BasePluginCommand {
	//vars
	private IRegistry explodeBreakRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.EXPLODE_BREAK_REGISTRY);
	
	//constructor
	public ExplodeBreakCommand() {
		super();
	}
	
	//public
	public void onLogin(String uuid, Player player) {
		explodeBreakRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_EXPLODEBREAK, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (explodeBreakRegistry.contains(uuid)) {
			sender.sendMessage("The next block " + player.getName() + " breaks will no longer explode.");
			explodeBreakRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage("The next block " + player.getName() + " breaks will now explode!");
			explodeBreakRegistry.setRegister(uuid, player);
		}
	}
}
