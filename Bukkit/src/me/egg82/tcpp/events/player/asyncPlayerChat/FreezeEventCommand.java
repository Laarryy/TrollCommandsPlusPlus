package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.registries.FreezeRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class FreezeEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IVariableRegistry<UUID> freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (freezeRegistry.hasRegister(player.getUniqueId()) && !CommandUtil.hasPermission(player, PermissionsType.CHAT_WHILE_FROZEN)) {
			event.setCancelled(true);
			player.sendMessage(LanguageUtil.getString(LanguageType.NO_CHAT_FROZEN));
		}
	}
}
