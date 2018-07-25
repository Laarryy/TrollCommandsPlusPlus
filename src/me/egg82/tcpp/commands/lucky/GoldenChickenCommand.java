package me.egg82.tcpp.commands.lucky;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Chicken;

import me.egg82.tcpp.core.LuckyCommand;
import me.egg82.tcpp.registries.LuckyChickenRegistry;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.events.VariableRegisterExpireEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;

public class GoldenChickenCommand extends LuckyCommand {
	//vars
	private IVariableExpiringRegistry<UUID> luckyChickenRegistry = ServiceLocator.getService(LuckyChickenRegistry.class);
	
	//constructor
	public GoldenChickenCommand() {
		super();
		
		luckyChickenRegistry.onExpire().attach((s, e) -> onExpire(e));
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Chicken c = player.getWorld().spawn(BlockUtil.getHighestSolidBlock(LocationUtil.getRandomPointAround(player.getLocation(), 1.5d)).add(0.0d, 1.0d, 0.0d), Chicken.class);
		luckyChickenRegistry.setRegister(c.getUniqueId(), c);
		c.setCustomName(ChatColor.GOLD + "Lucky Chicken");
		c.setCustomNameVisible(true);
	}
	
	private void onExpire(VariableRegisterExpireEventArgs<UUID> e) {
		Chicken c = (Chicken) e.getValue();
		
		c.setCustomNameVisible(false);
		c.setCustomName(null);
	}
}
