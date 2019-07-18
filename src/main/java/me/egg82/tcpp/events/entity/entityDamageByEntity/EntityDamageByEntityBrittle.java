package me.egg82.tcpp.events.entity.entityDamageByEntity;

import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import me.egg82.tcpp.services.CollectionProvider;
import me.egg82.tcpp.services.entity.EntityDamageHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageByEntityBrittle implements Consumer<EntityDamageByEntityEvent> {
    public EntityDamageByEntityBrittle() { }

    public void accept(EntityDamageByEntityEvent event) {
        Set<UUID> set = CollectionProvider.getSet("brittle");

        if (!set.contains(event.getEntity().getUniqueId())) {
            return;
        }

        LivingEntity entity = (LivingEntity) event.getEntity();
        entity.setHealth(0.0d);
        EntityDamageHandler.damage(entity, Double.MAX_VALUE, EntityDamageEvent.DamageCause.FALL);
    }
}
