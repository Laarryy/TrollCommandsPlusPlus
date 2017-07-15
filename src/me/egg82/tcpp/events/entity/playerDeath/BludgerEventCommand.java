package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BludgerBallRegistry;
import me.egg82.tcpp.services.BludgerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BludgerEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);
	private IRegistry bludgerBallRegistry = ServiceLocator.getService(BludgerBallRegistry.class);
	
	//constructor
	public BludgerEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getEntity();
		String uuid = player.getUniqueId().toString();
		
		if (!bludgerRegistry.hasRegister(uuid)) {
			return;
		}
		
		bludgerRegistry.setRegister(uuid, Player.class, null);
		
		bludgerBallRegistry.getRegister(uuid, Fireball.class).remove();
		bludgerBallRegistry.setRegister(uuid, Fireball.class, null);
	}
}
