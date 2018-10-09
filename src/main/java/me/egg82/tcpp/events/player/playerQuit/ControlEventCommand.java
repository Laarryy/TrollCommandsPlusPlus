package me.egg82.tcpp.events.player.playerQuit;

import java.util.UUID;

import me.egg82.tcpp.registries.ControlRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import me.egg82.tcpp.util.ControlHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ControlEventCommand extends EventHandler<PlayerQuitEvent> {
    //vars
    private IVariableRegistry<UUID> controlRegistry = ServiceLocator.getService(ControlRegistry.class);

    private ControlHelper controlHelper = ServiceLocator.getService(ControlHelper.class);

    //constructor
    public ControlEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (controlRegistry.hasRegister(uuid)) {
            // The player that quit/got kicked was controlling someone.
            player.sendMessage("Your controller has quit or been kicked!");
            controlHelper.uncontrol(uuid, player);
        }

        UUID controllerUuid = controlRegistry.getKey(player.getUniqueId());

        if (controllerUuid != null) {
            // The player that quit/got kicked was being controlled by someone.
            Player controller = CommandUtil.getPlayerByUuid(controllerUuid);
            if (controller != null) {
                controller.sendMessage(player.getName() + " has quit or been kicked!");
                controlHelper.uncontrol(controllerUuid, controller);
            }
        }
    }
}
