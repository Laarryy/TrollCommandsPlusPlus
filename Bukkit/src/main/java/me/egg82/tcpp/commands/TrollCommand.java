package me.egg82.tcpp.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainAbortAction;
import co.aikar.taskchain.TaskChainFactory;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.egg82.tcpp.APIException;
import me.egg82.tcpp.TrollAPI;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.Troll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.services.lookup.PlayerLookup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandAlias("troll|t")
public class TrollCommand extends BaseCommand {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Plugin plugin;
    private final TaskChainFactory taskFactory;

    private final TrollAPI api = TrollAPI.getInstance();

    private final Map<TrollType, Constructor<Troll>> trollConstructors = new HashMap<>();

    public TrollCommand(Plugin plugin, TaskChainFactory taskFactory) {
        this.plugin = plugin;
        this.taskFactory = taskFactory;
    }

    @Subcommand("alone")
    @CommandPermission("tcpp.command.alone")
    @Description("{@@alone.description}")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAlone(CommandIssuer issuer, String player) {
        getChain(issuer, player).syncLast(v -> startOrStopTroll(issuer, v, TrollType.ALONE)).execute();
    }

    @Subcommand("amnesia")
    @CommandPermission("tcpp.command.amnesia")
    @Description("{@@amnesia.description}")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAmnesia(CommandIssuer issuer, String player) {
        getChain(issuer, player).syncLast(v -> startOrStopTroll(issuer, v, TrollType.AMNESIA)).execute();
    }

    @Subcommand("annoy")
    @CommandPermission("tcpp.command.annoy")
    @Description("{@@annoy.description}")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAnnoy(CommandIssuer issuer, String player) {
        getChain(issuer, player).syncLast(v -> startOrStopTroll(issuer, v, TrollType.ANNOY)).execute();
    }

    private void startOrStopTroll(CommandIssuer issuer, UUID playerID, TrollType type) {
        Troll t = tryGetRunningTroll(playerID, type);
        try {
            if ((t == null || !api.stopTroll(t, issuer)) && isPlayerOnlineAndNotImmune(issuer, playerID)) {
                Constructor<Troll> c = trollConstructors.computeIfAbsent(type, k -> {
                    try {
                        Class<Troll> clazz = (Class<Troll>) getClass().getClassLoader().loadClass(type.getClassName());
                        Constructor<Troll> constructor = clazz.getConstructor(Plugin.class, UUID.class);
                        constructor.setAccessible(true);
                        return constructor;
                    } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                    return null;
                });
                if (c == null) {
                    issuer.sendError(Message.ERROR__INTERNAL);
                    return;
                }
                api.startTroll(c.newInstance(plugin, playerID), issuer);
            }
        } catch (APIException ex) {
            logger.error("[Hard: " + ex.isHard() + "] " + ex.getMessage(), ex);
            issuer.sendError(Message.ERROR__INTERNAL);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            issuer.sendError(Message.ERROR__INTERNAL);
        }
    }

    private Troll tryGetRunningTroll(UUID playerID, TrollType type) {
        for (Troll t : BukkitTroll.getActiveTrolls()) {
            if (playerID.equals(t.getPlayerID()) && type.equals(t.getType())) {
                return t;
            }
        }
        return null;
    }

    private TaskChain<UUID> getChain(CommandIssuer issuer, String player) {
        return taskFactory.newChain()
                .<UUID>asyncCallback((v, f) -> f.accept(getPlayerUUID(player)))
                .abortIfNull(new TaskChainAbortAction<Object, Object, Object>() {
                    @Override
                    public void onAbort(TaskChain<?> chain, Object arg1) {
                        issuer.sendError(Message.ERROR__PLAYER_NOT_FOUND, "{player}", player);
                    }
                });
    }

    private boolean isPlayerOnlineAndNotImmune(CommandIssuer issuer, UUID playerID) {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            issuer.sendError(Message.ERROR__PLAYER_OFFLINE);
            return false;
        }
        if (player.hasPermission("tcpp.immune")) {
            issuer.sendError(Message.ERROR__PLAYER_IMMUNE);
            return false;
        }

        return true;
    }

    private UUID getPlayerUUID(String name) {
        try {
            return PlayerLookup.get(name).getUUID();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            return null;
        }
    }

    @CatchUnknown @Default
    @CommandCompletion("@troll")
    public void onDefault(CommandIssuer issuer) {
        issuer.sendError(Message.ERROR__TROLL_NOT_FOUND);
        Bukkit.getServer().dispatchCommand(issuer.getIssuer(), "troll help");
    }

    @HelpCommand
    @Syntax("[command]")
    public void onHelp(CommandHelp help) { help.showHelp(); }
}
