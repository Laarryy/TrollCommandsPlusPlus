package me.egg82.tcpp.ticks;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.NightmareRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.core.protocol.IFakeLivingEntity;
import ninja.egg82.plugin.reflection.exceptionHandlers.IExceptionHandler;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;

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
				for (IFakeLivingEntity e : entities) {
					if (e.getLocation().distanceSquared(player.getLocation()) <= 1.0d) {
						e.attack(player, 1.0d);
					}
					
					e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23))));
					e.collideF(entities);
					e.lookTo(player.getEyeLocation());
				}
			}
		});
		ServiceLocator.getService(IExceptionHandler.class).addThread(runner);
		runner.start();
	}
}
