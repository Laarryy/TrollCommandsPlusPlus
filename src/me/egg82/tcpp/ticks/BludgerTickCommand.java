package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.BludgerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class BludgerTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);
	
	//constructor
	public BludgerTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : bludgerRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), bludgerRegistry.getRegister(key, Fireball.class));
		}
	}
	private void e(Player player, Fireball fireball) {
		if (player == null) {
			return;
		}
		
		fireball.setVelocity(player.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.35d));
	}
}