package me.egg82.tcpp.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.commands.PluginCommand;
import com.egg82.plugin.enums.CustomServiceType;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class DelayKillCommand extends PluginCommand {
	//vars
	
	//constructor
	public DelayKillCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_DELAYKILL)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 2) {
			try {
				delayKill(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
			} catch (Exception ex) {
				sender.sendMessage(MessageType.INCORRECT_USAGE);
				sender.getServer().dispatchCommand(sender, "help " + command.getName());
				dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
			}
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	private void delayKill(Player player, int delay) {
		if (player == null) {
			sender.sendMessage(MessageType.PLAYER_NOT_FOUND);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_NOT_FOUND);
			return;
		}
		if (permissionsManager.playerHasPermission(player, PermissionsType.IMMUNE)) {
			sender.sendMessage(MessageType.PLAYER_IMMUNE);
			dispatch(CommandEvent.ERROR, CommandErrorType.PLAYER_IMMUNE);
			return;
		}
		
		Timer t = new Timer(delay * 1000, onTimer);
		t.setRepeats(false);
		t.start();
		
		sender.sendMessage(player.getName() + "'s death is inevitable.");
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	
	private ActionListener onTimer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				return;
			}
			p.setHealth(0.0d);
		}
	};
}
