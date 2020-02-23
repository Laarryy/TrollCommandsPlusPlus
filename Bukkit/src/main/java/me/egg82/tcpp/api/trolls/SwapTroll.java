package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SwapTroll extends BukkitTroll {
    private UUID playerID2;
    public SwapTroll(UUID playerID, UUID playerID2, TrollType type) {
        super(playerID, type);

        this.playerID2 = playerID2;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        Player player2 = Bukkit.getPlayer(playerID2);
        if (player == null || player2 == null) {
            api.stopTroll(this, issuer);
            return;
        }

        Location loc = player.getLocation();
        player.teleportAsync(player2.getLocation());
        player2.teleportAsync(loc);
        issuer.sendInfo(Message.SWAP__START, "{player}", player.getName(), "{player2}", player2.getName());
        api.stopTroll(this, null);
    }
}
