package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class StarveCommand extends BasePluginCommand {
	//vars
	private IRegistry starveRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public StarveCommand() {
		super();
	}
	
	//public
	public void onDeath(String uuid, Player player) {
		starveRegistry.computeIfPresent(uuid, (k,v) -> {
			return null;
		});
	}
	public void onLogin(String uuid, Player player) {
		starveRegistry.computeIfPresent(uuid, (k,v) -> {
			return player;
		});
	}
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_STARVE, new int[]{1}, new int[]{0})) {
			Player player = Bukkit.getPlayer(args[0]);
			e(player.getUniqueId().toString(), player);
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(String uuid, Player player) {
		if (starveRegistry.contains(uuid)) {
			sender.sendMessage(player.getName() + " is no longer starving to death.");
			starveRegistry.setRegister(uuid, null);
		} else {
			sender.sendMessage(player.getName() + " is now starving to death.");
			starveRegistry.setRegister(uuid, player);
		}
	}
}
