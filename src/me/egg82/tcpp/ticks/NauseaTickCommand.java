package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.NauseaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class NauseaTickCommand extends TickCommand {
	//vars
	private IRegistry nauseaRegistry = (IRegistry) ServiceLocator.getService(NauseaRegistry.class);
	
	//constructor
	public NauseaTickCommand() {
		super();
		ticks = 200L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = nauseaRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) nauseaRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3), true);
	}
}
