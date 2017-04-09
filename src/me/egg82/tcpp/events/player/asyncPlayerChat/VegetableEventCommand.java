package me.egg82.tcpp.events.player.asyncPlayerChat;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.VegetableItemRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableItemRegistry = (IRegistry) ServiceLocator.getService(VegetableItemRegistry.class);
	
	//constructor
	public VegetableEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		Item groundItem = (Item) vegetableItemRegistry.getRegister(player.getUniqueId().toString());
		
		if (groundItem == null) {
			return;
		}
		
		if (!CommandUtil.hasPermission(player, PermissionsType.CHAT_WHILE_VEGETABLE)) {
			String type = groundItem.getItemStack().getType().name();
			int underscore = type.indexOf('_');
			
			if (underscore > -1) {
				type = type.substring(0, underscore);
			}
			type = type.toLowerCase();
			
			String finalMessage = "";
			// An approximation of the message's actual length, represented in a single string (such as "potato")
			int numStrings = (int) Math.floor(e.getMessage().length() / type.length());
			
			for (int i = 0; i < Math.max(1, numStrings); i++) {
				finalMessage += type + " ";
			}
			
			e.setMessage(finalMessage.trim());
		}
	}
}
