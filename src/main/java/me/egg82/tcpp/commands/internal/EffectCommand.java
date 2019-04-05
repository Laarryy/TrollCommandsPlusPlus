package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.databases.PotionTypeSearchDatabase;
import me.egg82.tcpp.registries.EffectRegistry;
import me.egg82.tcpp.registries.PotionNameRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.sql.LanguageDatabase;

public class EffectCommand extends CommandHandler {
    //vars
    private IVariableRegistry<UUID> effectRegistry = ServiceLocator.getService(EffectRegistry.class);
    private IVariableRegistry<String> potionNameRegistry = ServiceLocator.getService(PotionNameRegistry.class);

    private LanguageDatabase potionTypeDatabase = ServiceLocator.getService(PotionTypeSearchDatabase.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public EffectCommand() {
        super();
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
            ArrayList<String> retVal = new ArrayList<String>();

            if (args[1].isEmpty()) {
                for (String name : potionNameRegistry.getKeys()) {
                    retVal.add(potionNameRegistry.getRegister(name, String.class));
                }
            } else {
                for (String name : potionNameRegistry.getKeys()) {
                    String value = potionNameRegistry.getRegister(name, String.class);
                    if (value.toLowerCase().startsWith(args[1].toLowerCase())) {
                        retVal.add(value);
                    }
                }
            }

            return retVal;
        }

        return null;
    }

    //private
    @SuppressWarnings("unchecked")
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_EFFECT)) {
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

        if (args.length == 1) {
            Player player = CommandUtil.getPlayerByName(args[0]);
            if (player == null) {
                OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
                if (offlinePlayer != null) {
                    UUID uuid = offlinePlayer.getUniqueId();
                    if (effectRegistry.hasRegister(uuid)) {
                        eUndo(uuid, offlinePlayer);
                        return;
                    }
                }

                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            UUID uuid = player.getUniqueId();

            eUndo(uuid, player);
            return;
        }

        String search = "";
        for (int i = 1; i < args.length; i++) {
            search += args[i] + " ";
        }
        search = search.trim();

        PotionEffectType type = PotionEffectType.getByName(search.replaceAll(" ", "_").toUpperCase());

        if (type == null) {
            // Effect not found. It's possible it was just misspelled. Search the database.
            String[] types = potionTypeDatabase.getValues(potionTypeDatabase.naturalLanguage(search, false), 0);

            if (types == null || types.length == 0) {
                sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
                return;
            }

            type = PotionEffectType.getByName(types[0].toUpperCase());
            if (type == null) {
                sender.sendMessage(ChatColor.RED + "Searched type is invalid or was not found.");
                return;
            }
        }

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                UUID uuid = player.getUniqueId();

                List<PotionEffectType> currentEffects = (List<PotionEffectType>) effectRegistry.getRegister(uuid);

                if (currentEffects == null || !currentEffects.contains(type)) {
                    if (player.hasPermission(PermissionsType.IMMUNE)) {
                        continue;
                    }

                    e(uuid, player, type, currentEffects);
                } else {
                    eUndo(uuid, player, type, currentEffects);
                }
            }
        } else {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
                if (offlinePlayer != null) {
                    UUID uuid = offlinePlayer.getUniqueId();
                    if (effectRegistry.hasRegister(uuid)) {
                        List<PotionEffectType> currentEffects = effectRegistry.getRegister(uuid, List.class);
                        eUndo(uuid, offlinePlayer, type, currentEffects);
                        return;
                    }
                }

                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }

            UUID uuid = player.getUniqueId();

            List<PotionEffectType> currentEffects = effectRegistry.getRegister(uuid, List.class);

            if (currentEffects == null || !currentEffects.contains(type)) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    sender.sendMessage(ChatColor.RED + "Player is immune.");
                    return;
                }

                e(uuid, player, type, currentEffects);
            } else {
                eUndo(uuid, player, type, currentEffects);
            }
        }
    }

    private void e(UUID uuid, Player player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
        if (currentEffects == null) {
            currentEffects = new ArrayList<PotionEffectType>();
            effectRegistry.setRegister(uuid, currentEffects);
        }

        currentEffects.add(potionType);

        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " is now affected by " + potionNameRegistry.getRegister(potionType.getName()) + "!");
    }

    protected void onUndo() {
        Player player = CommandUtil.getPlayerByName(args[0]);
        if (player != null) {
            UUID uuid = player.getUniqueId();
            if (effectRegistry.hasRegister(uuid)) {
                eUndo(uuid, player);
            }
        } else {
            OfflinePlayer offlinePlayer = CommandUtil.getOfflinePlayerByName(args[0]);
            UUID uuid = offlinePlayer.getUniqueId();
            if (effectRegistry.hasRegister(uuid)) {
                eUndo(uuid, offlinePlayer);
            }
        }
    }

    private void eUndo(UUID uuid, Player player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
        currentEffects.remove(potionType);
        player.removePotionEffect(potionType);

        if (currentEffects.size() == 0) {
            effectRegistry.removeRegister(uuid);
        }

        sender.sendMessage(player.getName() + " is no longer being affected by " + potionNameRegistry.getRegister(potionType.getName(), String.class) + ".");
    }

    private void eUndo(UUID uuid, OfflinePlayer player, PotionEffectType potionType, List<PotionEffectType> currentEffects) {
        currentEffects.remove(potionType);

        if (currentEffects.size() == 0) {
            effectRegistry.removeRegister(uuid);
        }

        sender.sendMessage(player.getName() + " is no longer being affected by " + potionNameRegistry.getRegister(potionType.getName(), String.class) + ".");
    }

    @SuppressWarnings("unchecked")
    private void eUndo(UUID uuid, Player player) {
        if (effectRegistry.hasRegister(uuid)) {
            List<PotionEffectType> effects = effectRegistry.getRegister(uuid, List.class);
            for (PotionEffectType potionType : effects) {
                player.removePotionEffect(potionType);
            }
            effectRegistry.removeRegister(uuid);
        }

        sender.sendMessage(player.getName() + " no longer has any permanent potion effects.");
    }

    private void eUndo(UUID uuid, OfflinePlayer player) {
        effectRegistry.removeRegister(uuid);

        sender.sendMessage(player.getName() + " no longer has any permanent potion effects.");
    }
}