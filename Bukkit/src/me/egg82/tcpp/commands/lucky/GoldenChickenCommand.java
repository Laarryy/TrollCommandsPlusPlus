package me.egg82.tcpp.commands.lucky;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Chicken;

import me.egg82.tcpp.core.LuckyCommand;
import me.egg82.tcpp.services.registries.LuckyChickenRegistry;
import ninja.egg82.events.ExpireEventArgs;
import ninja.egg82.patterns.IExpiringRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;

public class GoldenChickenCommand extends LuckyCommand {
	//vars
	private IExpiringRegistry<UUID> luckyChickenRegistry = ServiceLocator.getService(LuckyChickenRegistry.class);
	
	//constructor
	public GoldenChickenCommand() {
		super();
		
		luckyChickenRegistry.onExpire().attach((s, e) -> onExpire(s, e));
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Chicken c = player.getWorld().spawn(BlockUtil.getTopWalkableBlock(LocationUtil.getRandomPointAround(player.getLocation(), 1.5d)), Chicken.class);
		luckyChickenRegistry.setRegister(c.getUniqueId(), c);
		c.setCustomName(ChatColor.GOLD + "Lucky Chicken");
		c.setCustomNameVisible(true);
	}
	
	private void onExpire(Object sender, ExpireEventArgs<UUID> e) {
		Chicken c = (Chicken) e.getValue();
		
		c.setCustomNameVisible(false);
		c.setCustomName(null);
	}
}
