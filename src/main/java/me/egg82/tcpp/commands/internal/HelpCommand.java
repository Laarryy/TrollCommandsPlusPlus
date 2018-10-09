package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.egg82.tcpp.databases.CommandSearchDatabase;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.tuples.pair.Pair;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.StringUtil;

public class HelpCommand extends CommandHandler {
    //vars
    private ArrayList<String> commandNames = new ArrayList<String>();
    private HashMap<String, Pair<String, String>> commands = new HashMap<String, Pair<String, String>>();
    private LanguageDatabase commandDatabase = ServiceLocator.getService(CommandSearchDatabase.class);

    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    //constructor
    public HelpCommand() {
        super();

        String[] list = ((String) ServiceLocator.getService(JavaPlugin.class).getDescription().getCommands().get("troll").get("usage")).replaceAll("\r\n", "\n").split("\n");
        for (String entry : list) {
            if (entry.contains("-= Available Commands =-")) {
                continue;
            }

            String usage = entry.substring(0, entry.indexOf(':')).trim();
            String c = usage.split(" ")[1];
            String description = entry.substring(entry.indexOf(':') + 1).trim();

            commandNames.add(c);
            commands.put(c, new Pair<String, String>(usage, description));
        }
    }

    //public
    public List<String> tabComplete() {
        if (args.length == 0 || args[0].isEmpty()) {
            return commandNames;
        } else if (args.length == 1) {
            ArrayList<String> retVal = new ArrayList<String>();
            for (int i = 0; i < commandNames.size(); i++) {
                if (commandNames.get(i).startsWith(args[0].toLowerCase())) {
                    retVal.add(commandNames.get(i));
                }
            }
            return retVal;
        }

        return null;
    }

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_HELP)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            e("help", commands.get("help"));
            return;
        }

        String search = "";
        for (int i = 0; i < args.length; i++) {
            search += args[i] + " ";
        }
        search = search.trim();

        String command = search.toLowerCase();
        Pair<String, String> commandValues = commands.get(command);

        if (commandValues == null) {
            // Might have simply misspelled the command. Search the database.
            String[] list = commandDatabase.getValues(commandDatabase.naturalLanguage(search, false), 0);

            if (list == null || list.length == 0) {
                sender.sendMessage(ChatColor.RED + "Command is invalid.");
                return;
            }

            command = list[0].toLowerCase();
            commandValues = commands.get(command);

            if (commandValues == null) {
                sender.sendMessage(ChatColor.RED + "Command is invalid.");
                return;
            }
        }

        e(command, commandValues);
    }

    private void e(String command, Pair<String, String> commandValues) {
        metricsHelper.commandWasRun(this);

        sender.sendMessage(ChatColor.YELLOW + "--------- " + ChatColor.WHITE + "Help: /troll " + command + " " + ChatColor.YELLOW + StringUtil.repeatChar('-', 25 - command.length()));
        sender.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.WHITE + commandValues.getRight());
        sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.WHITE + commandValues.getLeft());
    }

    protected void onUndo() {

    }
}
