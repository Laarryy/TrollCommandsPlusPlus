package me.egg82.tcpp.events.player.playerItemConsume;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.RandomPotionRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.reflection.type.TypeFilterHelper;
import ninja.egg82.utils.MathUtil;

public class RandomPotionEventCommand extends EventCommand<PlayerItemConsumeEvent> {
	//vars
	private IRegistry<UUID> randomPotionRegistry = ServiceLocator.getService(RandomPotionRegistry.class);
	
	private IPlayerHelper playerUtil = ServiceLocator.getService(IPlayerHelper.class);
	private PotionEffectType[] effects = null;
	
	//constructor
	public RandomPotionEventCommand(PlayerItemConsumeEvent event) {
		super(event);
		
		TypeFilterHelper<PotionEffectType> potionEffectTypeFilterHelper = new TypeFilterHelper<PotionEffectType>(PotionEffectType.class);
		effects = potionEffectTypeFilterHelper.getAllTypes();
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
