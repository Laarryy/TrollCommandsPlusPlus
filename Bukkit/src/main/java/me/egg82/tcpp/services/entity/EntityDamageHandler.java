package me.egg82.ae.services.entity;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageHandler {
    public static void damage(Damageable to, double damage, EntityDamageEvent.DamageCause cause) {
        Double d = Double.valueOf(damage);
        EntityDamageEvent damageEvent = new EntityDamageEvent(to, cause, new EnumMap<>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, d)),
                new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Functions.constant(d))));
        Bukkit.getPluginManager().callEvent(damageEvent);
        damageEvent.getEntity().setLastDamageCause(damageEvent);
        to.damage(damage);
    }

    public static void damage(Entity from, Damageable to, double damage, EntityDamageEvent.DamageCause cause) {
        Double d = Double.valueOf(damage);
        EntityDamageEvent damageEvent = new EntityDamageEvent(to, cause, new EnumMap<>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, d)),
                new EnumMap<EntityDamageEvent.DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(EntityDamageEvent.DamageModifier.BASE, Functions.constant(d))));
        Bukkit.getPluginManager().callEvent(damageEvent);
        damageEvent.getEntity().setLastDamageCause(damageEvent);
        to.damage(damage, from);
    }
}
