package me.egg82.tcpp.events.entity.potionSplash;

import java.util.Collection;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.RandomPotionRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.PotionEffectTypeHelper;
import ninja.egg82.utils.MathUtil;

public class RandomPotionEventCommand extends EventCommand {
	//vars
	private IRegistry randomPotionRegistry = (IRegistry) ServiceLocator.getService(RandomPotionRegistry.class);
	
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
		PotionSplashEvent e = (PotionSplashEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (!(e.getPotion().getShooter() instanceof Player)) {
			return;
		}
		
		if (randomPotionRegistry.hasRegister(((Player) e.getPotion().getShooter()).getUniqueId().toString())) {
			Collection<PotionEffect> potionEffects = e.getPotion().getEffects();
			
			int numEffects = potionEffects.size();
			potionEffects.clear();
			
			for (int i = 0; i < numEffects; i++) {
				PotionEffectType effect = effects[MathUtil.fairRoundedRandom(0, effects.length - 1)];
				potionEffects.add(new PotionEffect(effect, MathUtil.fairRoundedRandom(450, 9600), MathUtil.fairRoundedRandom(1, 5)));
			}
			
			for (LivingEntity entity : e.getAffectedEntities()) {
				for (PotionEffect potionEffect : potionEffects) {
					entity.addPotionEffect(potionEffect, true);
				}
			}
			
			e.setCancelled(true);
		}
	}
}
