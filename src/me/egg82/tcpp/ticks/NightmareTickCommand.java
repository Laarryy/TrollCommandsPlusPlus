package me.egg82.tcpp.ticks;

import java.util.List;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.reflection.protocol.IFakeLivingEntity;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;

public class NightmareTickCommand extends TickCommand {
	//vars
	private IRegistry nightmareRegistry = (IRegistry) ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = nightmareRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (List<IFakeLivingEntity>) nightmareRegistry.getRegister(name));
		}
	}
	private void e(String uuid, List<IFakeLivingEntity> entities) {
		Player player = CommandUtil.getPlayerByUuid(uuid);
		
		if (!player.isOnline()) {
			return;
		}
		
		new Thread(new Runnable() {
			public void run() {
				for (IFakeLivingEntity e : entities) {
					if (e.getLocation().distance(player.getLocation()) <= 1.0d) {
						e.attack(player, 1.0d);
					}
					
					e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23))));
					e.collide(entities);
					e.lookTo(player.getEyeLocation());
				}
			}
		}).start();
	}
}
