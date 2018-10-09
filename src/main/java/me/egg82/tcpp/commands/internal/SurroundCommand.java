package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.egg82.tcpp.databases.MonsterTypeSearchDatabase;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class SurroundCommand extends CommandHandler {
    //vars
    private ArrayList<String> mobNames = new ArrayList<String>();
    private LanguageDatabase monsterTypeDatabase = ServiceLocator.getService(MonsterTypeSearchDatabase.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public SurroundCommand() {
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
            if (!ReflectUtil.doesExtend(Monster.class, types[i].getEntityClass())) {
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
        if (!sender.hasPermission(PermissionsType.COMMAND_SURROUND)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        String search = "";
        for (int i = 1; i < args.length; i++) {
            search += args[i] + " ";
        }
        search = search.trim();

        EntityType type = null;

        try {
            type = EntityType.valueOf(search.toUpperCase().replaceAll(" ", "_"));
        } catch (Exception ex) {

        }

        if (type == null || !ReflectUtil.doesExtend(Monster.class, type.getEntityClass())) {
            // Not found or not a monster. Try to search the database.
            String[] types = monsterTypeDatabase.getValues(monsterTypeDatabase.naturalLanguage(search, false), 0);

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

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    continue;
                }

                e(player, type);
            }
        } else {
            Player player = CommandUtil.getPlayerByName(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return;
            }
            if (player.hasPermission(PermissionsType.IMMUNE)) {
                sender.sendMessage(ChatColor.RED + "Player is immune.");
                return;
            }

            e(player, type);
        }
    }

    private void e(Player player, EntityType mobType) {
        Location[] mobLocations = LocationUtil.getCircleAround(player.getLocation(), MathUtil.random(4.0d, 6.0d), MathUtil.fairRoundedRandom(8, 12));

        for (int i = 0; i < mobLocations.length; i++) {
            Location creatureLocation = BlockUtil.getHighestSolidBlock(mobLocations[i]).add(0.0d, 1.0d, 0.0d);

            Monster m = (Monster) player.getWorld().spawn(creatureLocation, mobType.getEntityClass());
            if (m instanceof PigZombie) {
                ((PigZombie) m).setAngry(true);
            }
            m.setTarget(player);
            Vector v = player.getLocation().toVector().subtract(creatureLocation.toVector()).normalize().multiply(0.23d);
            if (LocationUtil.isFinite(v)) {
                m.setVelocity(v);
            }
        }

        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " is now surrounded by " + mobType.name().replace('_', ' ').toLowerCase() + "s!");
    }

    protected void onUndo() {

    }
}
