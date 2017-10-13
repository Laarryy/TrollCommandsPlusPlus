package me.egg82.tcpp.ticks;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.services.registries.NightmareRegistry;
import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.protocol.core.IFakeLivingEntity;

public class NightmareTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> nightmareRegistry = ServiceLocator.getService(NightmareRegistry.class);
	
	//constructor
	public NightmareTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : nightmareRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), nightmareRegistry.getRegister(key, Collection.class));
		}
	}
	private void e(Player player, Collection<IFakeLivingEntity> entities) {
		if (player == null) {
			return;
		}
		
		Thread runner = new Thread(new Runnable() {
			public void run() {
				synchronized (entities) {
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
							e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(v)));
							e.collideF(entities);
						}
						e.lookTo(player.getEyeLocation());
					}
				}
				
				ServiceLocator.getService(IExceptionHandler.class).removeThread(Thread.currentThread());
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
	}
}
