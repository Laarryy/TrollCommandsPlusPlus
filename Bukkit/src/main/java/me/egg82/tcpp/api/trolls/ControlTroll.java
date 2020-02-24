package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.utils.DisguiseUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ControlTroll extends BukkitTroll {
    private final Plugin plugin;
    private CommandIssuer originalIssuer;
    private GameMode oldgm;
    private UUID controllerID;

    public ControlTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);

        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        originalIssuer = issuer;
        controllerID = issuer.getUniqueId();
        Player player = Bukkit.getPlayer(playerID);
        Player controller = Bukkit.getPlayer(controllerID);
        if (player == null || controller == null) {
            api.stopTroll(this, issuer);
            return;
        }

        DisguiseUtil.get().disguiseAsPlayer(controller, player);
        oldgm = player.getGameMode();
        player.setGameMode(GameMode.SPECTATOR);
        controller.teleport(player);
        player.setSpectatorTarget(controller);

        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::controlPlayer, 5, 20));

        issuer.sendInfo(Message.CONTROL__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);

        Player player = Bukkit.getPlayer(playerID);
        Player controller = Bukkit.getPlayer(issuer.getUniqueId());

        if (controller != null) {
            DisguiseUtil.get().undisguisePlayer(controller);
        }
        if (player != null) {
            player.setGameMode(oldgm);
            issuer.sendInfo(Message.CONTROL__STOP, "{player}", player.getName());
        }
    }

    private void controlPlayer() {
        Player player = Bukkit.getPlayer(playerID);
        Player controller = Bukkit.getPlayer(controllerID);
        if (player != null
                && !player.isDead()
                && player.isValid()
                && player.isOnline()
                && controller != null
                && !controller.isDead()
                && controller.isValid()
                && controller.isOnline()) {

            if (player.getGameMode() != GameMode.SPECTATOR) {
                player.setGameMode(GameMode.SPECTATOR);
            }
            if (player.getSpectatorTarget() == null || !player.getSpectatorTarget().equals(controller))
                player.setSpectatorTarget(controller);
        }
        else {
            try {
                api.stopTroll(playerID, TrollType.CONTROL, originalIssuer);
            }
            catch (APIException ex) {
                ex.printStackTrace();
            }
        }
    }
}
