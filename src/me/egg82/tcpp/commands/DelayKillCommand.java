package me.egg82.tcpp.commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import ninja.egg82.events.patterns.command.CommandEvent;

public class DelayKillCommand extends BasePluginCommand {
	//vars
	
	//constructor
	public DelayKillCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_DELAYKILL, new int[]{2}, new int[]{0})) {
			if (args.length == 2) {
				try {
					e(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
				} catch (Exception ex) {
					sender.sendMessage(MessageType.INCORRECT_USAGE);
					sender.getServer().dispatchCommand(sender, "help " + command.getName());
					dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
					return;
				}
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	private void e(Player player, int delay) {
		Timer t = new Timer(delay * 1000, onTimer);
		t.setRepeats(false);
		t.start();
		
		sender.sendMessage(player.getName() + "'s death is inevitable.");
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
