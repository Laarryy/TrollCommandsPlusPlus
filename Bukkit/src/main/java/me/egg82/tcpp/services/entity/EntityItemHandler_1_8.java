package me.egg82.ae.services.entity;

import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntityItemHandler_1_8 implements EntityItemHandler {
    public EntityItemHandler_1_8() { }

    public Optional<ItemStack> getItemInMainHand(LivingEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }

        ItemStack retVal = entity.getEquipment().getItemInHand();
        return (retVal == null || retVal.getType() == Material.AIR) ? Optional.empty() : Optional.of(retVal);
    }

    public Optional<ItemStack> getItemInOffHand(LivingEntity entity) { return Optional.empty(); }

    public void setItemInMainHand(LivingEntity entity, ItemStack item) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }

        entity.getEquipment().setItemInHand(item);
    }

    public void setItemInOffHand(LivingEntity entity, ItemStack item) { }
}
