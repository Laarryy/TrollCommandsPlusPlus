package me.egg82.tcpp.commands.internal;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.MobTypeSearchDatabase;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class SurroundCommand extends PluginCommand {
	//vars
	private LanguageDatabase mobTypeDatabase = (LanguageDatabase) ServiceLocator.getService(MobTypeSearchDatabase.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public SurroundCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_SURROUND)) {
			sender.sendMessage(SpigotMessageType.NO_PERMISSIONS);
			dispatch(CommandEvent.ERROR, SpigotCommandErrorType.NO_PERMISSIONS);
			return;
		}
		if (args.length < 2) {
			sender.sendMessage(SpigotMessageType.INCORRECT_USAGE);
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
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
		
		String search = "";
		for (int i = 1; i < args.length; i++) {
			search += args[i] + " ";
		}
		search = search.trim();
		
		EntityType type = EntityType.valueOf(search.toUpperCase().replaceAll(" ", "_"));
		
		if (type == null || !ReflectUtil.doesExtend(Monster.class, type.getEntityClass())) {
			// Not found or not a monster. Try to search the database.
			String[] types = mobTypeDatabase.getValues(mobTypeDatabase.naturalLanguage(search, false), 0);
			
			if (types == null || types.length == 0) {
				sender.sendMessage(MessageType.MOB_NOT_FOUND);
				dispatch(CommandEvent.ERROR, CommandErrorType.MOB_NOT_FOUND);
				return;
			}
			
			type = EntityType.valueOf(types[0].toUpperCase());
		}
		
		e(player.getUniqueId().toString(), player, type);
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player, EntityType mobType) {
		Location[] mobLocations = LocationUtil.getCircleAround(player.getLocation(), MathUtil.random(4.0d,  6.0d), MathUtil.fairRoundedRandom(8, 12));
		
		for (int i = 0; i < mobLocations.length; i++) {
			Location creatureLocation = BlockUtil.getTopWalkableBlock(mobLocations[i]);
			
			Monster m = (Monster) player.getWorld().spawn(creatureLocation, mobType.getEntityClass());
			if (m instanceof PigZombie) {
				((PigZombie) m).setAngry(true);
			}
			m.setTarget(player);
			m.setVelocity(player.getLocation().toVector().subtract(creatureLocation.toVector()).normalize().multiply(0.23d));
		}
		
		metricsHelper.commandWasRun(this);
		
		sender.sendMessage(player.getName() + " is now surrounded by " + mobType.name().replace('_', ' ').toLowerCase() + "s!");
	}
	
	protected void onUndo() {
		
	}
}
