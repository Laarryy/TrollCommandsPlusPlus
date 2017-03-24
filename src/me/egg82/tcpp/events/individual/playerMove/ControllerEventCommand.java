package me.egg82.tcpp.events.individual.playerMove;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.IPermissionsManager;
import ninja.egg82.registry.interfaces.IRegistry;

public class ControllerEventCommand extends EventCommand {
	//vars
	private IRegistry controllerRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
	private IPermissionsManager permissionsManager = (IPermissionsManager) ServiceLocator.getService(SpigotServiceType.PERMISSIONS_MANAGER);
	
	//constructor
	public ControllerEventCommand(Event event) {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		String uuid = e.getPlayer().getUniqueId().toString();
		
		if (controllerRegistry.contains(uuid)) {
			Player p = (Player) controllerRegistry.getRegister(uuid);
			moveTo(e, e.getPlayer(), p);
		}
	}
	private void moveTo(PlayerMoveEvent e, Player originalPlayer, Player newPlayer) {
		if (!permissionsManager.playerHasPermission(newPlayer, PermissionsType.FREECAM_WHILE_CONTROLLED)) {
			newPlayer.teleport(originalPlayer);
		}
	}
}
