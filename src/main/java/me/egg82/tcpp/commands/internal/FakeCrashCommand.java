package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.CommandHandler;
import ninja.egg82.utils.MathUtil;

public class FakeCrashCommand extends CommandHandler {
    //vars
    private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);

    private String[] messages = new String[]{
            "Internal Exception: java.net.SocketException: Connection reset",
            "Internal Exception: java.io.IOException: An existing connection was forcibly closed by the remote host",
            "Internal Exception: io.netty.handler.timeout.ReadTimeoutException",
            "Internal Exception: java.io.IOException: Bad packet id 83",
            "Internal Exception: io.netty.handler.codec.DecoderException: java.lang.IndexOutOfBoundsException: readerIndex(5) + length(558) exceeds writerIndex(9): UnpooledHeapByteBuf(ridx: 5, widx: 9, cap: 9)",
            "Internal Exception: java.io.IOException: Received string length longer than maximum allowed (258 > 256)",
            "Internal Exception: io.netty.handler.codec.DecoderException: java.util.zip.DataFormatException: incorrect header check",
            "Internal Exception: java.io.IOException: An established connection was aborted by the software in your host machine",
            "Internal Exception: java.net.SocketTimeoutException: Read timed out",
            "Internal Exception: io.netty.handler.timeout.ReadTimeoutException",
            "[Proxy] Lost connection to server.",
            "Internal Exception: io.netty.handler.codec.DecoderException: java.lang.NullPointerException",
            "Internal Exception: java.net.SocketException: Software caused connection abort: recv failed",
            "Internal Exception: java.net.SocketException: Software caused connection abort: socket write error",
            "Failed to login: Bad login",
            "Disconnected",
            "Internal Exception: java.lang.IllegalArgumentException: Parameter 'directory' is not a directory",
            "Internal Exception: io.netty.handler.codec.DecoderException: java.io.IOException: Packet was larger than I expected, found 1 bytes extra whilst reading packet 63",
            "Timed out",
            "Internal Exception: io.netty.handler.codec.DecoderException: com.google.gson.JsonSyntaxException: com.google.gson.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1",
            "A fatal error has occured, this connection is terminated",
            "End of stream",
            "Internal Exception: java.lang.NullPointerException",
            "Internal Exception: io.netty.handler.codec.DecoderException: java.io.EOFException: fieldSize is too long! Length is 25184, but maximum is 458",
            "Internal server error",
            "Server closed"
    };

    //constructor
    public FakeCrashCommand() {
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
        }

        return null;
    }

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!sender.hasPermission(PermissionsType.COMMAND_FAKE_CRASH)) {
            sender.sendMessage(ChatColor.RED + "You do not have permissions to run this command!");
            return;
        }
        if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
            String name = getClass().getSimpleName();
            name = name.substring(0, name.length() - 7).toLowerCase();
            Bukkit.getServer().dispatchCommand((CommandSender) sender.getHandle(), "troll help " + name);
            return;
        }

        List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer((CommandSender) sender.getHandle()) ? ((Player) sender.getHandle()).getLocation() : null));
        if (players.size() > 0) {
            for (Player player : players) {
                if (player.hasPermission(PermissionsType.IMMUNE)) {
                    continue;
                }

                e(player);
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

            e(player);
        }
    }

    private void e(Player player) {
        player.kickPlayer(messages[MathUtil.fairRoundedRandom(0, messages.length - 1)]);

        metricsHelper.commandWasRun(this);

        sender.sendMessage(player.getName() + " has been \"crashed\".");
    }

    protected void onUndo() {

    }
}
