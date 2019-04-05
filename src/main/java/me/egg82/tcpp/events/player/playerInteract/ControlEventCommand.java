package me.egg82.tcpp.events.player.playerInteract;

import java.util.UUID;

import me.egg82.tcpp.registries.ControlRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ControlEventCommand extends EventHandler<PlayerInteractEvent> {
    //vars
    private IVariableRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);

    //constructor
    public ControlEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        UUID controllerUuid = controlRegistry.getKey(player.getUniqueId());
        if (controllerUuid != null) {
            // Player is being controlled by someone
            if (!player.hasPermission(PermissionsType.FREECAM_WHILE_CONTROLLED)) {
                event.setCancelled(true);
            }
        }
    }
}