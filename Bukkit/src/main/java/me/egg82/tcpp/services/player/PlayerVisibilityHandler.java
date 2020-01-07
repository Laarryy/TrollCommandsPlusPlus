package me.egg82.tcpp.services.player;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface PlayerVisibilityHandler {
    void hide(Plugin plugin, Player from, Player who);
    void show(Plugin plugin, Player from, Player who);
}
