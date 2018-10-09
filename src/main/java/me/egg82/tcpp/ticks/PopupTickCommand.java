package me.egg82.tcpp.ticks;

import java.util.UUID;

import me.egg82.tcpp.registries.PopupRegistry;
import org.bukkit.entity.Player;

import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class PopupTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> popupRegistry = ServiceLocator.getService(PopupRegistry.class);

    //constructor
    public PopupTickCommand() {
        super(0L, 20L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : popupRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        if (Math.random() <= 0.1d) {
            player.openInventory(player.getInventory());
        } else if (Math.random() <= 0.1d) {
            player.closeInventory();
        }
    }
}
