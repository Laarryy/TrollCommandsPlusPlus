package me.egg82.tcpp.events.individual.playerChat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class ControllerEventCommand extends EventCommand {
	//vars
	private IRegistry controllerRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
	
	//constructor
	public ControllerEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		String name = e.getPlayer().getName().toLowerCase();
		
		if (controllerRegistry.contains(name)) {
			Player p = (Player) controllerRegistry.getRegister(name);
			sendMessageAs(e, e.getPlayer(), p);
		}
	}
	private void sendMessageAs(AsyncPlayerChatEvent e, Player originalPlayer, Player newPlayer) {
		if (newPlayer == null) {
			controllerRegistry.setRegister(originalPlayer.getName(), null);
			return;
		}
		
		newPlayer.chat(e.getMessage());
		e.setCancelled(true);
	}
}
