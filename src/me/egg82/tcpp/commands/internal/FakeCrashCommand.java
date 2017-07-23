package me.egg82.tcpp.commands.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.LanguageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.exceptions.PlayerImmuneException;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.PlayerNotFoundException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;
import ninja.egg82.utils.MathUtil;

public class FakeCrashCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	private String[] messages = new String[] {
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
	public FakeCrashCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	public List<String> tabComplete(CommandSender sender, Command command, String label, String[] args) {
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
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_FAKE_CRASH)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_FAKE_CRASH)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		List<Player> players = CommandUtil.getPlayers(CommandUtil.parseAtSymbol(args[0], CommandUtil.isPlayer(sender) ? ((Player) sender).getLocation() : null));
		if (players.size() > 0) {
			for (Player player : players) {
				if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
					continue;
				}
				
				e(player.getUniqueId(), player);
			}
		} else {
			Player player = CommandUtil.getPlayerByName(args[0]);
			
			if (player == null) {
				sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.PLAYER_NOT_FOUND));
				onError().invoke(this, new ExceptionEventArgs<PlayerNotFoundException>(new PlayerNotFoundException(args[0])));
				return;
			}
			if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
				sender.sendMessage(LanguageUtil.getString(LanguageType.PLAYER_IMMUNE));
				onError().invoke(this, new ExceptionEventArgs<PlayerImmuneException>(new PlayerImmuneException(player)));
				return;
			}
			
			e(player.getUniqueId(), player);
		}
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(UUID uuid, Player player) {
		player.kickPlayer(messages[MathUtil.fairRoundedRandom(0, messages.length - 1)]);
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " has been \"crashed\".");
	}
	
	protected void onUndo() {
		
	}
}
