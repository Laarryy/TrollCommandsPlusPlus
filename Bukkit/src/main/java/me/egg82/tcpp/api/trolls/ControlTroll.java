package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.utils.DisguiseUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ControlTroll extends BukkitTroll {
    private static final Map<Player, Player> puppets = new ConcurrentHashMap<>();
    private final UUID issuerID;

    public ControlTroll(UUID playerID, UUID issuerID, TrollType type) {
        super(playerID, type);

        this.issuerID = issuerID;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        Player controller = Bukkit.getPlayer(issuerID);
        if (player == null || controller == null) {
            api.stopTroll(this, issuer);
            return;
        }

        if (puppets.containsKey(controller))
            api.stopTroll(puppets.get(controller).getUniqueId(), TrollType.CONTROL, issuer);

        DisguiseUtil.get().disguiseAsPlayer(controller, player);
        player.setGameMode(GameMode.SPECTATOR);
        controller.teleport(player);
        player.setSpectatorTarget(controller);
        puppets.put(controller, player);

        issuer.sendInfo(Message.CONTROL__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        Player controller = Bukkit.getPlayer(issuerID);
        if (player == null || controller == null) {
            return;
        }

        puppets.remove(controller);
        DisguiseUtil.get().undisguisePlayer(controller);
        player.setGameMode(GameMode.SURVIVAL);
        issuer.sendInfo(Message.CONTROL__STOP, "{player}", player.getName());
    }

    public static Map<Player, Player> getPuppets() {
        return puppets;
    }
}
