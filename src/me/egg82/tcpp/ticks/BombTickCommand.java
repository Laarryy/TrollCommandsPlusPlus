package me.egg82.tcpp.ticks;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;
import com.egg82.utils.MathUtil;

import me.egg82.tcpp.enums.PluginServiceType;

public class BombTickCommand extends Command {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BOMB_REGISTRY);
	
	//constructor
	public BombTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = bombRegistry.registryNames();
		for (String name : names) {
			shoot((Player) bombRegistry.getRegister(name));
		}
	}
	
	private void shoot(Player player) {
		int rand = (int) (MathUtil.random(5.0d, 10.0d));
		Vector vec = player.getLocation().getDirection().multiply(3.0d);
		for (int i = 0; i < rand; i++) {
			Fireball fireball = (Fireball) player.getWorld().spawn(player.getLocation().subtract(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Fireball.class);
			fireball.setVelocity(vec);
		}
	}
}