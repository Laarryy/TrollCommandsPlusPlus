package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import me.egg82.tcpp.registries.ControlRegistry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class ControlEventCommand extends EventHandler<AsyncPlayerChatEvent> {
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
        UUID playerUuid = player.getUniqueId();

        if (controlRegistry.hasRegister(playerUuid)) {
            // Player is controlling someone
            Player controlledPlayer = CommandUtil.getPlayerByUuid(controlRegistry.getRegister(playerUuid, UUID.class));

            if (controlledPlayer != null) {
                controlledPlayer.chat(event.getMessage());
                event.setCancelled(true);
            }
        }

        UUID controllerUuid = controlRegistry.getKey(playerUuid);
        if (controllerUuid != null) {
            // Player is being controlled by someone
            if (!player.hasPermission(PermissionsType.CHAT_WHILE_CONTROLLED)) {
                player.sendMessage(ChatColor.RED + "You do not have permissions to chat while being controlled!");
                event.setCancelled(true);
            }
        }
    }
}
