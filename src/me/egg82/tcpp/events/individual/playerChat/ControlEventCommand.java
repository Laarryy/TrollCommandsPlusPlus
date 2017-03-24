package me.egg82.tcpp.events.individual.playerChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.enums.SpigotServiceType;
import ninja.egg82.plugin.utils.interfaces.IPermissionsManager;
import ninja.egg82.registry.interfaces.IRegistry;

public class ControlEventCommand extends EventCommand {
	//vars
	private IRegistry controlRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
	private IPermissionsManager permissionsManager = (IPermissionsManager) ServiceLocator.getService(SpigotServiceType.PERMISSIONS_MANAGER);
	
	//constructor
	public ControlEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		Player player = e.getPlayer();
		
		if (controlRegistry.contains(player.getUniqueId().toString())) {
			if (!permissionsManager.playerHasPermission(player, PermissionsType.CHAT_WHILE_CONTROLLED)) {
				e.setCancelled(true);
			}
		}
	}
}
