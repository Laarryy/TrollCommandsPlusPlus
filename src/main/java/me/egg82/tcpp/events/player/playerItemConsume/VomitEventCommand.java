package me.egg82.tcpp.events.player.playerItemConsume;

import java.util.UUID;

import me.egg82.tcpp.registries.VomitRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class VomitEventCommand extends EventHandler<PlayerItemConsumeEvent> {
    //vars
    private IVariableRegistry<UUID> vomitRegistry = ServiceLocator.getService(VomitRegistry.class);

    private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);

    //constructor
    public VomitEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        if (vomitRegistry.hasRegister(player.getUniqueId())) {
            ItemStack items = entityHelper.getItemInMainHand(player);

            if (items == null || items.getType() == Material.AIR) {
                items = event.getItem();
            }

            ItemStack droppedItem = new ItemStack(items);
            droppedItem.setAmount(1);

            int itemsAmount = items.getAmount();

            if (itemsAmount == 1) {
                entityHelper.setItemInMainHand(player, null);
            } else {
                items.setAmount(itemsAmount - 1);
                entityHelper.setItemInMainHand(player, items);
            }

            player.getWorld().dropItemNaturally(BlockUtil.getHighestSolidBlock(LocationUtil.getLocationInFront(player.getLocation(), MathUtil.random(3.0d, 5.0d), false)).add(0.0d, 1.0d, 0.0d), droppedItem);

            event.setCancelled(true);
        }
    }
}
