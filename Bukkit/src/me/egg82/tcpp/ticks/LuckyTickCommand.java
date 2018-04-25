package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.registries.LuckyChickenRegistry;
import me.egg82.tcpp.services.registries.LuckyVillagerRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableExpiringRegistry;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;

public class LuckyTickCommand extends TickCommand {
	//vars
	private IVariableExpiringRegistry<UUID> luckyChickenRegistry = ServiceLocator.getService(LuckyChickenRegistry.class);
	private IVariableExpiringRegistry<UUID> luckyVillagerRegistry = ServiceLocator.getService(LuckyVillagerRegistry.class);
	
	//constructor
	public LuckyTickCommand() {
		super(75L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : luckyChickenRegistry.getKeys()) {
			e(luckyChickenRegistry.getRegister(key, Chicken.class));
		}
		for (UUID key : luckyVillagerRegistry.getKeys()) {
			e(luckyVillagerRegistry.getRegister(key, Villager.class));
		}
	}
	private void e(Chicken chicken) {
		if (chicken == null) {
			return;
		}
		
		if (Math.random() <= 0.55) {
			chicken.getWorld().dropItemNaturally(chicken.getLocation(), new ItemStack(Material.GOLD_INGOT, MathUtil.fairRoundedRandom(1, 3)));
		}
	}
	private void e(Villager villager) {
		if (villager == null) {
			return;
		}
		
		if (Math.random() <= 0.55) {
			villager.getWorld().dropItemNaturally(villager.getLocation(), new ItemStack(Material.EMERALD, MathUtil.fairRoundedRandom(1, 3)));
		}
	}
}