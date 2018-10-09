package me.egg82.tcpp.events.player.playerInteract;

import java.util.UUID;

import me.egg82.tcpp.registries.LagBlockRegistry;
import me.egg82.tcpp.registries.LagRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventHandler<PlayerInteractEvent> {
    //vars
    private IVariableRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
    private IVariableRegistry<Location> lagBlockRegistry = ServiceLocator.getService(LagBlockRegistry.class);

    //constauctor
    public LagEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        Block block = event.getClickedBlock();
        // Are we clicking on air?
        if (block == null || block.getType() == Material.AIR) {
            return;
        }

        Location blockLocation = block.getLocation();

        // Block is currently being lagged. Nobody should interact with it.
        if (lagBlockRegistry.hasRegister(blockLocation)) {
            event.setCancelled(true);
            return;
        }
        if (!lagRegistry.hasRegister(player.getUniqueId())) {
            return;
        }

        // Create a "snapshot" of what's happening
        BlockState blockState = block.getState();
        Action action = event.getAction();

        // Either BlockBreakEvent or BlockPlaceEvent
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR || (action == Action.RIGHT_CLICK_BLOCK && !(blockState instanceof InventoryHolder))) {
            return;
        }

        event.setCancelled(true);
        lagBlockRegistry.setRegister(blockLocation, null);

        // Manually doing the event after 1.5-2.5 seconds
        TaskUtil.runSync(new Runnable() {
            public void run() {
                // Opened inventory event
                if (action == Action.RIGHT_CLICK_BLOCK && blockState instanceof InventoryHolder) {
                    player.openInventory(((InventoryHolder) blockState).getInventory());
                }

                lagBlockRegistry.removeRegister(blockLocation);
            }
        }, MathUtil.fairRoundedRandom(30, 50));
    }
}
