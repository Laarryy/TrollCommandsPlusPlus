package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class BombTickCommand extends TickCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	
	//constructor
	public BombTickCommand() {
		super();
		ticks = 10l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = reg.registryNames();
		for (String name : names) {
			e((Player) reg.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		int rand = (int) (MathUtil.random(5.0d, 10.0d));
		Location pl = player.getLocation().clone();
		Vector vec = null;
		Location fl = null;
		for (int i = 0; i < rand; i++) {
			Fireball fireball = (Fireball) player.getWorld().spawn(pl.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Fireball.class);
			fl = fireball.getLocation();
			vec = new Vector(pl.getX() - fl.getX(), pl.getY() - fl.getY(), pl.getZ() - fl.getZ());
			fireball.setVelocity(vec.normalize().multiply(2.0d));
		}
	}
}