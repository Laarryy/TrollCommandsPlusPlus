package me.egg82.tcpp.events.entity.entityDeath;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import me.egg82.tcpp.registries.HydraMobRegistry;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;

import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class HydraEventCommand extends EventHandler<EntityDeathEvent> {
    //vars
    private IVariableRegistry<UUID> hydraMobRegistry = ServiceLocator.getService(HydraMobRegistry.class);

    //constructor
    public HydraEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        LivingEntity entity = event.getEntity();
        UUID uuid = entity.getUniqueId();

        if (hydraMobRegistry.hasRegister(uuid)) {
            Location[] mobLocations = LocationUtil.getCircleAround(entity.getLocation(), MathUtil.random(2.0d, 3.0d), MathUtil.fairRoundedRandom(2, 4));

            for (int i = 0; i < mobLocations.length; i++) {
                Location creatureLocation = BlockUtil.getHighestSolidBlock(mobLocations[i]).add(0.0d, 1.0d, 0.0d);

                LivingEntity e = (LivingEntity) entity.getWorld().spawn(creatureLocation, event.getEntityType().getEntityClass());
                clone(entity, e);
                if (e instanceof PigZombie) {
                    ((PigZombie) e).setAngry(true);
                }
                if (e instanceof Monster) {
                    ((Monster) e).setTarget(event.getEntity().getKiller());
                }

                hydraMobRegistry.setRegister(e.getUniqueId(), uuid);
            }
        }
    }

    private void clone(LivingEntity from, LivingEntity to) {
        to.setCustomName(from.getCustomName());
        to.setCustomNameVisible(from.isCustomNameVisible());
        to.setGlowing(from.isGlowing());
        to.setGravity(from.hasGravity());
        to.setInvulnerable(from.isInvulnerable());
        to.setSilent(from.isSilent());
        to.setAI(from.hasAI());
        to.setCanPickupItems(from.getCanPickupItems());
        to.setCollidable(from.isCollidable());
        to.setMaximumAir(from.getMaximumAir());
        to.setMaximumNoDamageTicks(from.getMaximumNoDamageTicks());
        to.setRemoveWhenFarAway(from.getRemoveWhenFarAway());

        Collection<PotionEffect> effects = from.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            to.addPotionEffect(effect, true);
        }

        Set<String> tags = from.getScoreboardTags();
        for (String tag : tags) {
            to.addScoreboardTag(tag);
        }
    }
}
