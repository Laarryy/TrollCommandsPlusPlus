package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.databases.MobTypeSearchDatabase;
import me.egg82.tcpp.registries.SpawnBreakRegistry;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.ReflectUtil;

public class SpawnBreakCommand extends CommandHandler {
    //vars
    private IVariableRegistry<UUID> spawnBreakRegistry = ServiceLocator.getService(SpawnBreakRegistry.class);
    private ArrayList<String> mobNames = new ArrayList<String>();
    private LanguageDatabase mobTypeDatabase = ServiceLocator.getService(MobTypeSearchDatabase.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public SpawnBreakCommand() {
        super();

        EntityType[] types = EntityType.values();

        Arrays.sort(types, (a, b) -> {
            if (a == null) {
                if (b == null) {
                    return 0;
                }
                return -1;
            }
            if (b == null) {
                return 1;
            }

            return a.name().compareTo(b.name());
        });

        for (int i = 0; i < types.length; i++) {
            if (types[i] == null) {
                continue;
            }
            if (!ReflectUtil.doesExtend(Creature.class, types[i].getEntityClass())) {
                continue;
            }

            mobNames.add(WordUtils.capitalize(String.join(" ", types[i].name().toLowerCase().split("_"))));
        }
    }

    //public
    public List<String> tabComplete() {
        if (args.length == 1) {
            ArrayList<String> retVal = new ArrayList<String>();

            if (args[0].isEmpty()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    retVal.add(player.getName());
                }
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                        retVal.add(player.getName());
                    }
                }
            }

            return retVal;
        } else if (args.length == 2) {
            if (args[1].isEmpty()) {
                return mobNames;
            }

            ArrayList<String> retVal = new ArrayList<String>();
            for (int i = 0; i < mobNames.size(); i++) {
                if (mobNames.get(i).toLowerCase().startsWith(args[1].toLowerCase())) {
                    retVal.add(mobNames.get(i));
                }
            }
            return retVal;
        }

        return null;
    }

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_SPAWN_BREAK)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        EntityType type = null;

        if (args.length > 1) {
            String search = "";
            for (int i = 1; i < args.length; i++) {
                search += args[i] + " ";
            }
            search = search.trim();

            try {
                type = EntityType.valueOf(search.toUpperCase().replaceAll(" ", "_"));
            } catch (Exception ex) {

            }

            if (type == null) {
                // Not found. Try to search the database.
                String[] types = mobTypeDatabase.getValues(mobTypeDatabase.naturalLanguage(search, false), 0);

                if (types == null || types.length == 0) {
                    sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
                    return;
                }

                try {
                    type = EntityType.valueOf(types[0].toUpperCase());
                } catch (Exception ex) {

                }
                if (type == null) {
                    sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
                    return;
                }
            }
        }

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                UUID uuid = player.getUniqueId();

                if (type != null && !type.equals(spawnBreakRegistry.getRegister(uuid, EntityType.class))) {
                    if (player.hasPermission(PermissionsType.IMMUNE)) {
                        continue;
                    }

                    e(uuid, player, type);
                } else {
                    eUndo(uuid, player);
                }
            }
        } else {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
                if (offlinePlayer != null) {
                    UUID uuid = offlinePlayer.getUniqueId();
                    if (spawnBreakRegistry.hasRegister(uuid)) {
                        eUndo(uuid, offlinePlayer);
                        return;
                    }
                }

                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            UUID uuid = player.getUniqueId();

            if (type != null && !type.equals(spawnBreakRegistry.getRegister(uuid, EntityType.class))) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    sender.sendMessage(ChatColor.RED + "Player is immune.");
                    return;
                }

                e(uuid, player, type);
            } else {
                if (!spawnBreakRegistry.hasRegister(uuid)) {
                    sender.sendMessage(ChatColor.RED + "The target you've chosen is invalid.");
                    return;
                }

                eUndo(uuid, player);
            }
        }
    }

    private void e(UUID uuid, Player player, EntityType mobType) {
        spawnBreakRegistry.setRegister(uuid, mobType);
        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " will now have " + mobType.name().replace('_', ' ').toLowerCase() + "s drop on them as they break blocks!");
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            if (spawnBreakRegistry.hasRegister(uuid)) {
                eUndo(uuid, player);
            }
        } else {
            OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
            UUID uuid = offlinePlayer.getUniqueId();
            if (spawnBreakRegistry.hasRegister(uuid)) {
                eUndo(uuid, offlinePlayer);
            }
        }
    }

    private void eUndo(UUID uuid, Player player) {
        spawnBreakRegistry.removeRegister(uuid);

        sender.sendMessage(player.getName() + " will no longer have mobs drop on them.");
    }

    private void eUndo(UUID uuid, OfflinePlayer player) {
        spawnBreakRegistry.removeRegister(uuid);

        sender.sendMessage(player.getName() + " will no longer have mobs drop on them.");
    }
}