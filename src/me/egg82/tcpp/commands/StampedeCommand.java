package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.egg82.events.patterns.command.CommandEvent;
import com.egg82.plugin.commands.PluginCommand;
import com.egg82.plugin.utils.BlockUtil;
import com.egg82.utils.MathUtil;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;

public class StampedeCommand extends PluginCommand {
	//vars
	
	//constructor
	public StampedeCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (sender instanceof Player && !permissionsManager.playerHasPermission((Player) sender, PermissionsType.COMMAND_STAMPEDE)) {
			sender.sendMessage(MessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_PERMISSIONS);
			return;
		}
		
		if (args.length == 1) {
			stampede(Bukkit.getPlayer(args[0]));
		} else {
			sender.sendMessage(MessageType.INCORRECT_USAGE);
			sender.getServer().dispatchCommand(sender, "help " + command.getName());
			dispatch(CommandEvent.ERROR, CommandErrorType.INCORRECT_USAGE);
		}
	}
	private void stampede(Player player) {
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
		
		int rand = MathUtil.fairRoundedRandom(10, 20);
		Location tloc = player.getLocation();
		Location loc = BlockUtil.getTopAirBlock(new Location(tloc.getWorld(), MathUtil.random(tloc.getX() - 5.0d, tloc.getX() + 5.0d), tloc.getY(), MathUtil.random(tloc.getZ() - 5.0d, tloc.getZ() + 5.0d)));
		Vector vel = loc.clone().subtract(tloc).toVector().normalize().multiply(3.0d);
		for (int i = 0; i < rand; i++) {
			spawnCow(player, loc, vel);
		}
		
		sender.sendMessage("The angry cows have been unleashed on " + player.getName() + ".");
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void spawnCow(Player p, Location l, Vector v) {
		Cow cow = (Cow) p.getWorld().spawn(l, Cow.class);
		Silverfish fish = (Silverfish) p.getWorld().spawn(l, Silverfish.class);
		fish.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 3), true);
		fish.setPassenger(cow);
		fish.setVelocity(v);
		cow.setVelocity(v);
		fish.setTarget(p);
	}
}
