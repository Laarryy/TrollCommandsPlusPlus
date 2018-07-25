package me.egg82.tcpp.commands.lucky;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Villager;

import me.egg82.tcpp.core.LuckyCommand;
import me.egg82.tcpp.registries.LuckyVillagerRegistry;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.events.VariableRegisterExpireEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;

public class SpawnVillagerCommand extends LuckyCommand {
	//vars
	private IVariableExpiringRegistry<UUID> luckyVillagerRegistry = ServiceLocator.getService(LuckyVillagerRegistry.class);
	
	//constructor
	public SpawnVillagerCommand() {
		super();
		
		luckyVillagerRegistry.onExpire().attach((s, e) -> onExpire(e));
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Villager v = player.getWorld().spawn(BlockUtil.getHighestSolidBlock(LocationUtil.getRandomPointAround(player.getLocation(), 1.5d)).add(0.0d, 1.0d, 0.0d), Villager.class);
		luckyVillagerRegistry.setRegister(v.getUniqueId(), v);
		v.setCustomName(ChatColor.GOLD + "Lucky Villager");
		v.setCustomNameVisible(true);
	}
	
	private void onExpire(VariableRegisterExpireEventArgs<UUID> e) {
		Villager v = (Villager) e.getValue();
		
		v.setCustomNameVisible(false);
		v.setCustomName(null);
	}
}
