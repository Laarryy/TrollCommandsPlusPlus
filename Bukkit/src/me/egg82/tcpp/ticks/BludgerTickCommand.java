package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.registries.BludgerRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class BludgerTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);
	
	//constructor
	public BludgerTickCommand() {
		super(0L, 2L);
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
		
		Vector v = player.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.35d);
		if (LocationUtil.isFinite(v)) {
			fireball.setVelocity(v);
		}
	}
}