package me.egg82.tcpp.ticks;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class AnnoyTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.ANNOY_REGISTRY);
	private Sound[] sounds = null;
	
	//constructor
	public AnnoyTickCommand() {
		super();
		
		sounds  = new Sound[]{
				Sound.ENTITY_VILLAGER_AMBIENT,
				Sound.ENTITY_VILLAGER_TRADING,
				Sound.ENTITY_VILLAGER_NO,
				Sound.ENTITY_VILLAGER_YES
		};
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
		if(player == null) {
			return;
		}
		
		if (Math.random() <= 0.2d) {
			player.getWorld().playSound(player.getLocation(), sounds[MathUtil.fairRoundedRandom(0, sounds.length - 1)], 1.0f, 1.0f);
		}
	}
}
