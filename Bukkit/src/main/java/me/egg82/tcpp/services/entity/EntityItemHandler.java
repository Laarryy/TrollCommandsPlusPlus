package me.egg82.ae.services.entity;

import java.util.Optional;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface EntityItemHandler {
    Optional<ItemStack> getItemInMainHand(LivingEntity entity);
    Optional<ItemStack> getItemInOffHand(LivingEntity entity);

    void setItemInMainHand(LivingEntity entity, ItemStack item);
    void setItemInOffHand(LivingEntity entity, ItemStack item);
}
