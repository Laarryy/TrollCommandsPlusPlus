package me.egg82.tcpp.events.entity.entityExplode;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.egg82.tcpp.services.BludgerBallRegistry;
import me.egg82.tcpp.services.BludgerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BludgerEventCommand extends EventCommand {
	//vars
	private IRegistry bludgerRegistry = (IRegistry) ServiceLocator.getService(BludgerRegistry.class);
	private IRegistry bludgerBallRegistry = (IRegistry) ServiceLocator.getService(BludgerBallRegistry.class);
	
	//constructor
	public BludgerEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		EntityExplodeEvent e = (EntityExplodeEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		String uuid = bludgerBallRegistry.getName(e.getEntity());
		if (uuid != null) {
			bludgerRegistry.setRegister(uuid, Player.class, null);
			bludgerBallRegistry.setRegister(uuid, Fireball.class, null);
		}
	}
}
