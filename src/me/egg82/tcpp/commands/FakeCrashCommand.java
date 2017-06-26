package me.egg82.tcpp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class FakeCrashCommand extends PluginCommand {
	//vars
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
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
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_FAKE_CRASH)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 1)) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.INCORRECT_USAGE);
			return;
		}
		
		Player player = CommandUtil.getPlayerByName(args[0]);
		
		if (player == null) {
			sender.sendMessage(SpigotMessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (CommandUtil.hasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		e(player.getUniqueId().toString(), player);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		player.kickPlayer(messages[MathUtil.fairRoundedRandom(0, messages.length - 1)]);
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage(player.getName() + " has been \"crashed\".");
	}
	
	protected void onUndo() {
		
	}
}
