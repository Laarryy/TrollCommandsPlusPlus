package me.egg82.tcpp.events.player.playerMove;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.registries.MidasTouchRegistry;
import me.egg82.tcpp.rollback.reflection.IRollbackHelper;
import me.egg82.tcpp.util.DisplayHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.patterns.tuples.pair.Boolean2Pair;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.protocol.reflection.IFakeBlockHelper;

public class MidasTouchEventCommand extends EventHandler<PlayerMoveEvent> {
	//vars
	private IRegistry<UUID, Boolean2Pair<Material>> midasTouchRegistry = ServiceLocator.getService(MidasTouchRegistry.class);
	
	private IRollbackHelper rollbackHelper = ServiceLocator.getService(IRollbackHelper.class);
	private DisplayHelper displayHelper = ServiceLocator.getService(DisplayHelper.class);
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public MidasTouchEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("deprecation")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String name = player.getName();
		Boolean2Pair<Material> params = midasTouchRegistry.getRegister(player.getUniqueId());
		
		if (params == null) {
			return;
		}
		
		Location playerLoc = player.getLocation();
		Location playerEyeLoc = player.getEyeLocation();
		
		Set<Location> blocks = displayHelper.getBlockLocationsAround(playerLoc);
		for (Location l : blocks) {
			if (!l.getBlock().getType().isSolid()) {
				continue;
			}
			if (l.getBlock().getType() == params.getLeft()) {
				continue;
			}
			
			l.add(0.5d, 0.0d, 0.5d);
			if (playerLoc.distanceSquared(l) <= 1.5625 || playerEyeLoc.distanceSquared(l) <= 1.5625) { //1.25
				if (params.getRight()) {
					Block b = l.getBlock();
					rollbackHelper.logBlockRemove(name, l, b.getType(), b.getData());
					b.setType(params.getLeft());
					rollbackHelper.logBlockPlace(name, l, params.getLeft(), (byte) 0);
				} else {
					fakeBlockHelper.updateBlock(player, l, params.getLeft());
				}
			}
		}
	}
}
