package me.egg82.tcpp.events.player.playerItemConsume;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.registries.RandomPotionRegistry;
import ninja.egg82.bukkit.reflection.player.IPlayerHelper;
import ninja.egg82.filters.EnumFilter;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class RandomPotionEventCommand extends EventHandler<PlayerItemConsumeEvent> {
	//vars
	private IVariableRegistry<UUID> randomPotionRegistry = ServiceLocator.getService(RandomPotionRegistry.class);
	
	private IPlayerHelper playerUtil = ServiceLocator.getService(IPlayerHelper.class);
	private PotionEffectType[] effects = null;
	
	//constructor
	public RandomPotionEventCommand() {
		super();
		
		EnumFilter<PotionEffectType> potionEffectTypeFilterHelper = new EnumFilter<PotionEffectType>(PotionEffectType.class);
		effects = potionEffectTypeFilterHelper.build();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (randomPotionRegistry.hasRegister(player.getUniqueId())) {
			ItemStack items = playerUtil.getItemInMainHand(player);
			
			if (items.getType() == Material.POTION) {
				int itemsAmount = items.getAmount();
				
				if (itemsAmount == 1) {
					playerUtil.setItemInMainHand(player, null);
				} else {
					items.setAmount(itemsAmount - 1);
					playerUtil.setItemInMainHand(player, items);
				}
				
				player.addPotionEffect(new PotionEffect(effects[MathUtil.fairRoundedRandom(0, effects.length - 1)], MathUtil.fairRoundedRandom(450, 9600), MathUtil.fairRoundedRandom(1, 5)), true);
				
				event.setCancelled(true);
			}
		}
	}
}
