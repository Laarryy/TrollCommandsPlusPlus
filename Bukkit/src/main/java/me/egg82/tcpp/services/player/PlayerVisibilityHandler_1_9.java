package me.egg82.tcpp.services.player;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerVisibilityHandler_1_9 implements PlayerVisibilityHandler {
    public void hide(Plugin plugin, Player from, Player who) { from.hidePlayer(plugin, who); }

    public void show(Plugin plugin, Player from, Player who) { from.showPlayer(plugin, who); }
}
