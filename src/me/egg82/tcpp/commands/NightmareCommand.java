package me.egg82.tcpp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.NightmareRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CommandEvent;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotCommandErrorType;
import ninja.egg82.plugin.enums.SpigotMessageType;
import ninja.egg82.plugin.reflection.protocol.IFakeEntityHelper;
import ninja.egg82.plugin.reflection.protocol.IFakeLivingEntity;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class NightmareCommand extends PluginCommand {
	//vars
	private IRegistry nightmareRegistry = (IRegistry) ServiceLocator.getService(NightmareRegistry.class);
	
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	private IFakeEntityHelper fakeEntityHelper = (IFakeEntityHelper) ServiceLocator.getService(IFakeEntityHelper.class);
	
	private String gameVersion = (String) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("game.version");
	
	//constructor
	public NightmareCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_NIGHTMARE)) {
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
		if (!fakeEntityHelper.isValidLibrary()) {
			sender.sendMessage(MessageType.NO_LIBRARY);
			dispatch(CommandEvent.ERROR, CommandErrorType.NO_LIBRARY);
			return;
		}
		if (gameVersion == "1.8" || gameVersion == "1.8.3" || gameVersion == "1.8.8") {
			sender.sendMessage(MessageType.WRONG_GAME_VERSION);
			dispatch(CommandEvent.ERROR, CommandErrorType.WRONG_GAME_VERSION);
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
		
		String uuid = player.getUniqueId().toString();
		
		if (!nightmareRegistry.hasRegister(uuid)) {
			e(uuid, player);
		} else {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	private void e(String uuid, Player player) {
		ArrayList<IFakeLivingEntity> entities = new ArrayList<IFakeLivingEntity>();
		
		new Thread(new Runnable() {
			public void run() {
				Location[] zombieLocs = LocationUtil.getCircleAround(player.getLocation(), 3, MathUtil.fairRoundedRandom(6, 9));
				Location[] zombie2Locs = LocationUtil.getCircleAround(player.getLocation(), 5, MathUtil.fairRoundedRandom(8, 12));
				
				for (int i = 0; i < zombieLocs.length; i++) {
					IFakeLivingEntity e = fakeEntityHelper.createEntity(zombieLocs[i], EntityType.ZOMBIE);
					e.addPlayer(player);
					e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23))));
					e.lookTo(player.getEyeLocation());
					entities.add(e);
				}
				for (int i = 0; i < zombie2Locs.length; i++) {
					IFakeLivingEntity e = fakeEntityHelper.createEntity(zombie2Locs[i], EntityType.ZOMBIE);
					e.addPlayer(player);
					e.moveTo(BlockUtil.getTopWalkableBlock(e.getLocation().add(player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23))));
					e.lookTo(player.getEyeLocation());
					entities.add(e);
				}
			}
		}).start();
		
		nightmareRegistry.setRegister(uuid, List.class, entities);
		
		metricsHelper.commandWasRun(command.getName());
		
		sender.sendMessage(player.getName() + " is now living in a nightmare!");
	}
	
	protected void onUndo() {
		Player player = CommandUtil.getPlayerByName(args[0]);
		String uuid = player.getUniqueId().toString();
		
		if (nightmareRegistry.hasRegister(uuid)) {
			eUndo(uuid, player);
		}
		
		dispatch(CommandEvent.COMPLETE, null);
	}
	@SuppressWarnings("unchecked")
	private void eUndo(String uuid, Player player) {
		List<IFakeLivingEntity> entities = (List<IFakeLivingEntity>) nightmareRegistry.getRegister(uuid);
		
		new Thread(new Runnable() {
			public void run() {
				for (IFakeLivingEntity e : entities) {
					e.destroy();
				}
			}
		}).start();
		
		nightmareRegistry.setRegister(uuid, List.class, null);
		
		sender.sendMessage(player.getName() + " is no longer living in a nightmare.");
	}
}
