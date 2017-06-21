package me.egg82.tcpp.events.player.playerItemConsume;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.RandomPotionRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.player.IPlayerUtil;
import ninja.egg82.plugin.utils.PotionEffectTypeHelper;
import ninja.egg82.utils.MathUtil;

public class RandomPotionEventCommand extends EventCommand {
	//vars
	private IRegistry randomPotionRegistry = (IRegistry) ServiceLocator.getService(RandomPotionRegistry.class);
	
	private IPlayerUtil playerUtil = (IPlayerUtil) ServiceLocator.getService(IPlayerUtil.class);
	private PotionEffectTypeHelper potionEffectTypeHelper = (PotionEffectTypeHelper) ServiceLocator.getService(PotionEffectTypeHelper.class);
	private PotionEffectType[] effects = null;
	
	//constructor
	public RandomPotionEventCommand(Event event) {
		super(event);
		effects = potionEffectTypeHelper.getAllEffectTypes();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerItemConsumeEvent e = (PlayerItemConsumeEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (randomPotionRegistry.hasRegister(player.getUniqueId().toString())) {
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
				
				e.setCancelled(true);
			}
		}
	}
}
