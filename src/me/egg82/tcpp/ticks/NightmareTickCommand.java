package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.registries.NightmareRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.concurrent.IConcurrentDeque;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.protocol.core.IFakeLivingEntity;
import ninja.egg82.utils.ThreadUtil;

public class NightmareTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareTickCommand() {
		super(0L, 2L);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : nightmareRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), nightmareRegistry.getRegister(key, IConcurrentDeque.class));
		}
	}
	private void e(Player player, IConcurrentDeque<IFakeLivingEntity> entities) {
		if (player == null) {
			return;
		}
		
		ThreadUtil.submit(new Runnable() {
			public void run() {
				for (IFakeLivingEntity e : entities) {
					if (!e.getLocation().getWorld().equals(player.getWorld())) {
						continue;
					}
					
					if (e.getLocation().distanceSquared(player.getLocation()) <= 1.0d) {
						TaskUtil.runSync(new Runnable() {
							public void run() {
								e.attack(player, 1.0d);
							}
						});
					}
					
					Vector v = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23);
					if (LocationUtil.isFinite(v)) {
						e.moveTo(BlockUtil.getHighestSolidBlock(e.getLocation().add(v)).add(0.0d, 1.0d, 0.0d));
						e.collideF(entities);
					}
					e.lookTo(player.getEyeLocation());
				}
			}
		});
	}
}
