package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class SpartaTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.SPARTA_REGISTRY);
	
	//constructor
	public SpartaTickCommand() {
		super();
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
		Location pl = player.getLocation();
		Vector vec = null;
		Location al = null;
		for (int i = 0; i < rand; i++) {
			Arrow arrow = (Arrow) player.getWorld().spawn(player.getLocation().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Arrow.class);
			al = arrow.getLocation();
			vec = new Vector(pl.getX() - al.getX(), pl.getY() - al.getY(), pl.getZ() - al.getZ());
			arrow.setVelocity(vec.normalize().multiply(2.0d));
		}
	}
}
