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
import ninja.egg82.utils.MathUtil;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
	private IPermissionsManager permissionsManager = (IPermissionsManager) ServiceLocator.getService(SpigotServiceType.PERMISSIONS_MANAGER);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		Player player = e.getPlayer();
		
		if (vegetableRegistry.contains(player.getUniqueId().toString())) {
			if (!permissionsManager.playerHasPermission(player, PermissionsType.CHAT_WHILE_VEGETABLE)) {
				String potato = "";
				int num = MathUtil.fairRoundedRandom(1, 6);
				
				for (int i = 0; i < num; i++) {
					potato += "potato ";
				}
				potato = potato.trim();
				
				e.setMessage(potato);
			}
		}
	}
}
