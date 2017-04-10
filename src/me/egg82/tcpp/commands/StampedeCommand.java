package me.egg82.tcpp.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.reflection.entity.IEntityUtil;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class StampedeCommand extends PluginCommand {
	//vars
	private IEntityUtil entityUtil = (IEntityUtil) ServiceLocator.getService(IEntityUtil.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public StampedeCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_STAMPEDE)) {
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
		int numCows = MathUtil.fairRoundedRandom(10, 20);
		Location playerLocation = player.getLocation().clone();
		Location herdLocation = BlockUtil.getTopAirBlock(new Location(playerLocation.getWorld(), MathUtil.random(playerLocation.getX() - 5.0d, playerLocation.getX() + 5.0d), playerLocation.getY(), MathUtil.random(playerLocation.getZ() - 5.0d, playerLocation.getZ() + 5.0d)));
		Vector cowVelocity = herdLocation.clone().subtract(playerLocation).toVector().normalize().multiply(3.0d);
		
		for (int i = 0; i < numCows; i++) {
			spawnCow(player, herdLocation, cowVelocity);
		}
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage("The angry cows have been unleashed on " + player.getName() + ".");
	}
	private void spawnCow(Player player, Location location, Vector velocity) {
		Cow c = player.getWorld().spawn(location, Cow.class);
		Silverfish f = player.getWorld().spawn(location, Silverfish.class);
		f.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 3), true);
		
		entityUtil.addPassenger(f, c);
		
		f.setVelocity(velocity);
		c.setVelocity(velocity);
		f.setTarget(player);
	}
}
