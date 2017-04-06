package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.DelayKillRegistry;
import me.egg82.tcpp.services.DelayKillTicksRegistry;
import me.egg82.tcpp.services.DelayKillTimeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DelayKillEventCommand extends EventCommand {
	//vars
	private IRegistry delayKillRegistry = (IRegistry) ServiceLocator.getService(DelayKillRegistry.class);
	private IRegistry delayKillTimeRegistry = (IRegistry) ServiceLocator.getService(DelayKillTimeRegistry.class);
	private IRegistry delayKillTicksRegistry = (IRegistry) ServiceLocator.getService(DelayKillTicksRegistry.class);
	
	//constructor
	public DelayKillEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		String uuid = e.getEntity().getUniqueId().toString();
		
		delayKillRegistry.setRegister(uuid, Player.class, null);
		delayKillTimeRegistry.setRegister(uuid, long.class, null);
		delayKillTicksRegistry.setRegister(uuid, long.class, null);
	}
}
