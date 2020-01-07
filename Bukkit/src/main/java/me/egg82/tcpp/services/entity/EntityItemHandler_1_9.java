package me.egg82.ae.services.entity;

import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntityItemHandler_1_9 implements EntityItemHandler {
    public EntityItemHandler_1_9() { }

    public Optional<ItemStack> getItemInMainHand(LivingEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }

        ItemStack retVal = entity.getEquipment().getItemInMainHand();
        return (retVal == null || retVal.getType() == Material.AIR) ? Optional.empty() : Optional.of(retVal);
    }

    public Optional<ItemStack> getItemInOffHand(LivingEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }

        ItemStack retVal = entity.getEquipment().getItemInOffHand();
        return (retVal == null || retVal.getType() == Material.AIR) ? Optional.empty() : Optional.of(retVal);
    }

    public void setItemInMainHand(LivingEntity entity, ItemStack item) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }

        entity.getEquipment().setItemInMainHand(item);
    }

    public void setItemInOffHand(LivingEntity entity, ItemStack item) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }

        entity.getEquipment().setItemInOffHand(item);
    }
}
