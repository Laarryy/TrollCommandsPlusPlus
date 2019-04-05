package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import me.egg82.tcpp.registries.GarbleRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.StringUtil;

public class GarbleEventCommand extends EventHandler<AsyncPlayerChatEvent> {
    //vars
    private IVariableRegistry<UUID> garbleRegistry = ServiceLocator.getService(GarbleRegistry.class);

    //constructor
    public GarbleEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        if (garbleRegistry.hasRegister(player.getUniqueId())) {
            String oldMessage = String.format(event.getFormat(), player.getDisplayName(), event.getMessage());
            event.setMessage(StringUtil.shuffle(event.getMessage()));
            event.getRecipients().remove(player);
            player.sendMessage(oldMessage);
        }
    }
}