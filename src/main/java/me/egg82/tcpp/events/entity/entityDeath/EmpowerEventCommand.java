package me.egg82.tcpp.events.entity.entityDeath;

import java.util.UUID;

import me.egg82.tcpp.registries.EmpowerEntityRegistry;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class EmpowerEventCommand extends EventHandler<EntityDeathEvent> {
    //vars
    private IVariableRegistry<UUID> empowerEntityRegistry = ServiceLocator.getService(EmpowerEntityRegistry.class);

    //constructor
    public EmpowerEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        LivingEntity entity = event.getEntity();
        UUID uuid = entity.getUniqueId();

        if (empowerEntityRegistry.hasRegister(uuid)) {
            entity.removePotionEffect(PotionEffectType.ABSORPTION);
            entity.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            entity.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            entity.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            entity.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            entity.removePotionEffect(PotionEffectType.REGENERATION);
            entity.removePotionEffect(PotionEffectType.SPEED);

            empowerEntityRegistry.removeRegister(uuid);
        }
    }
}
