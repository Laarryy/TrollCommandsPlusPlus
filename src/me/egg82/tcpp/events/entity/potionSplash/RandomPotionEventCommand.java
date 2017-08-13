package me.egg82.tcpp.events.entity.potionSplash;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableCollection;

import me.egg82.tcpp.services.RandomPotionRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.type.TypeFilterHelper;
import ninja.egg82.utils.MathUtil;

public class RandomPotionEventCommand extends EventCommand<PotionSplashEvent> {
	//vars
	private IRegistry<UUID> randomPotionRegistry = ServiceLocator.getService(RandomPotionRegistry.class);
	
	private PotionEffectType[] effects = null;
	
	//constructor
	public RandomPotionEventCommand(PotionSplashEvent event) {
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
		
		if (!(event.getPotion().getShooter() instanceof Player)) {
			return;
		}
		
		if (randomPotionRegistry.hasRegister(((Player) event.getPotion().getShooter()).getUniqueId())) {
			Collection<PotionEffect> potionEffects = event.getPotion().getEffects();
			
			if (potionEffects instanceof ImmutableCollection) {
				return;
			}
			
			int numEffects = potionEffects.size();
			potionEffects.clear();
			
			for (int i = 0; i < numEffects; i++) {
				PotionEffectType effect = effects[MathUtil.fairRoundedRandom(0, effects.length - 1)];
				potionEffects.add(new PotionEffect(effect, MathUtil.fairRoundedRandom(450, 9600), MathUtil.fairRoundedRandom(1, 5)));
			}
			
			for (LivingEntity entity : event.getAffectedEntities()) {
				for (PotionEffect potionEffect : potionEffects) {
					entity.addPotionEffect(potionEffect, true);
				}
			}
			
			event.setCancelled(true);
		}
	}
}
