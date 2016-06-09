package me.egg82.tcpp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcstats.Metrics;

import me.egg82.tcpp.commands.AloneCommand;
import me.egg82.tcpp.commands.AnnoyCommand;
import me.egg82.tcpp.commands.BanishCommand;
import me.egg82.tcpp.commands.BombCommand;
import me.egg82.tcpp.commands.BurnCommand;
import me.egg82.tcpp.commands.CannonCommand;
import me.egg82.tcpp.commands.CometCommand;
import me.egg82.tcpp.commands.ControlCommand;
import me.egg82.tcpp.commands.CreepCommand;
import me.egg82.tcpp.commands.DelayKillCommand;
import me.egg82.tcpp.commands.ElectrifyCommand;
import me.egg82.tcpp.commands.EntombCommand;
import me.egg82.tcpp.commands.FlipCommand;
import me.egg82.tcpp.commands.FreezeCommand;
import me.egg82.tcpp.commands.GarbleCommand;
import me.egg82.tcpp.commands.GolemCommand;
import me.egg82.tcpp.commands.HauntCommand;
import me.egg82.tcpp.commands.HoundCommand;
import me.egg82.tcpp.commands.HurtCommand;
import me.egg82.tcpp.commands.InfinityCommand;
import me.egg82.tcpp.commands.LagCommand;
import me.egg82.tcpp.commands.LavaBreakCommand;
import me.egg82.tcpp.commands.LiftCommand;
import me.egg82.tcpp.commands.LureCommand;
import me.egg82.tcpp.commands.NauseaCommand;
import me.egg82.tcpp.commands.NightCommand;
import me.egg82.tcpp.commands.PortalCommand;
import me.egg82.tcpp.commands.PotatoCommand;
import me.egg82.tcpp.commands.RewindCommand;
import me.egg82.tcpp.commands.SlapCommand;
import me.egg82.tcpp.commands.SlowMineCommand;
import me.egg82.tcpp.commands.SlowpokeCommand;
import me.egg82.tcpp.commands.SpamCommand;
import me.egg82.tcpp.commands.SpartaCommand;
import me.egg82.tcpp.commands.SpinCommand;
import me.egg82.tcpp.commands.StampedeCommand;
import me.egg82.tcpp.commands.StarveCommand;
import me.egg82.tcpp.commands.SwapCommand;
import me.egg82.tcpp.commands.TrollCommand;
import me.egg82.tcpp.commands.VaporizeCommand;
import me.egg82.tcpp.commands.VegetableCommand;
import me.egg82.tcpp.commands.VoidCommand;
import me.egg82.tcpp.commands.WeaklingCommand;
import me.egg82.tcpp.commands.ZombifyCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.events.AsyncPlayerChatEventCommand;
import me.egg82.tcpp.events.BlockBreakEventCommand;
import me.egg82.tcpp.events.BlockPlaceEventCommand;
import me.egg82.tcpp.events.ItemDespawnEventCommand;
import me.egg82.tcpp.events.PlayerDeathEventCommand;
import me.egg82.tcpp.events.PlayerInteractEventCommand;
import me.egg82.tcpp.events.PlayerKickEventCommand;
import me.egg82.tcpp.events.PlayerMoveEventCommand;
import me.egg82.tcpp.events.PlayerPickupItemEventCommand;
import me.egg82.tcpp.events.PlayerQuitEventCommand;
import me.egg82.tcpp.ticks.AloneTickCommand;
import me.egg82.tcpp.ticks.AnnoyTickCommand;
import me.egg82.tcpp.ticks.BombTickCommand;
import me.egg82.tcpp.ticks.BurnTickCommand;
import me.egg82.tcpp.ticks.ElectrifyTickCommand;
import me.egg82.tcpp.ticks.HauntTickCommand;
import me.egg82.tcpp.ticks.HurtTickCommand;
import me.egg82.tcpp.ticks.NauseaTickCommand;
import me.egg82.tcpp.ticks.RewindTickCommand;
import me.egg82.tcpp.ticks.SlowMineTickCommand;
import me.egg82.tcpp.ticks.SlowpokeTickCommand;
import me.egg82.tcpp.ticks.SpamTickCommand;
import me.egg82.tcpp.ticks.SpartaTickCommand;
import me.egg82.tcpp.ticks.SpinTickCommand;
import me.egg82.tcpp.ticks.StarveTickCommand;
import me.egg82.tcpp.ticks.VegetableTickCommand;
import me.egg82.tcpp.ticks.WeaklingTickCommand;
import me.egg82.tcpp.util.RegistryUtil;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;
import net.gravitydevelopment.updater.Updater.UpdateType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.registry.Registry;
import ninja.egg82.utils.Util;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private Timer updateTimer = null;
	
	//constructor
	public TrollCommandsPlusPlus() {
		super();
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		Object[] enums = Util.getStaticFields(PluginServiceType.class);
		String[] services = Arrays.copyOf(enums, enums.length, String[].class);
		for (String s : services) {
			ServiceLocator.provideService(s, Registry.class);
		}
		
		RegistryUtil.intialize();
		
		updateTimer = new Timer(24 * 60 * 60 * 1000, onUpdateTimer);
	}
	
	public void onEnable() {
		super.onEnable();
		
		try {
			Metrics m = new Metrics(this);
			m.start();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		checkUpdate();
		updateTimer.setRepeats(true);
		updateTimer.start();
		
		commandHandler.addCommand("banish", BanishCommand.class);
		commandHandler.addCommand("bomb", BombCommand.class);
		commandHandler.addCommand("cannon", CannonCommand.class);
		commandHandler.addCommand("comet", CometCommand.class);
		commandHandler.addCommand("creep", CreepCommand.class);
		commandHandler.addCommand("electrify", ElectrifyCommand.class);
		commandHandler.addCommand("entomb", EntombCommand.class);
		commandHandler.addCommand("freeze", FreezeCommand.class);
		commandHandler.addCommand("garble", GarbleCommand.class);
		commandHandler.addCommand("haunt", HauntCommand.class);
		commandHandler.addCommand("lift", LiftCommand.class);
		commandHandler.addCommand("lure", LureCommand.class);
		commandHandler.addCommand("slap", SlapCommand.class);
		commandHandler.addCommand("slowpoke", SlowpokeCommand.class);
		commandHandler.addCommand("spin", SpinCommand.class);
		commandHandler.addCommand("stampede", StampedeCommand.class);
		commandHandler.addCommand("swap", SwapCommand.class);
		commandHandler.addCommand("troll", TrollCommand.class);
		commandHandler.addCommand("vaporize", VaporizeCommand.class);
		commandHandler.addCommand("weakling", WeaklingCommand.class);
		commandHandler.addCommand("zombify", ZombifyCommand.class);
		commandHandler.addCommand("burn", BurnCommand.class);
		commandHandler.addCommand("spam", SpamCommand.class);
		commandHandler.addCommand("delaykill", DelayKillCommand.class);
		commandHandler.addCommand("potato", PotatoCommand.class);
		commandHandler.addCommand("starve", StarveCommand.class);
		commandHandler.addCommand("hurt", HurtCommand.class);
		commandHandler.addCommand("void", VoidCommand.class);
		commandHandler.addCommand("slowmine", SlowMineCommand.class);
		commandHandler.addCommand("nausea", NauseaCommand.class);
		commandHandler.addCommand("control", ControlCommand.class);
		commandHandler.addCommand("vegetable", VegetableCommand.class);
		commandHandler.addCommand("hound", HoundCommand.class);
		commandHandler.addCommand("infinity", InfinityCommand.class);
		commandHandler.addCommand("lavabreak", LavaBreakCommand.class);
		commandHandler.addCommand("golem", GolemCommand.class);
		commandHandler.addCommand("portal", PortalCommand.class);
		commandHandler.addCommand("flip", FlipCommand.class);
		commandHandler.addCommand("alone", AloneCommand.class);
		commandHandler.addCommand("annoy", AnnoyCommand.class);
		commandHandler.addCommand("sparta", SpartaCommand.class);
		commandHandler.addCommand("night", NightCommand.class);
		commandHandler.addCommand("rewind", RewindCommand.class);
		commandHandler.addCommand("lag", LagCommand.class);
		
		eventListener.addEvent(PlayerDeathEvent.class, PlayerDeathEventCommand.class);
		eventListener.addEvent(PlayerQuitEvent.class, PlayerQuitEventCommand.class);
		eventListener.addEvent(PlayerKickEvent.class, PlayerKickEventCommand.class);
		eventListener.addEvent(PlayerMoveEvent.class, PlayerMoveEventCommand.class);
		eventListener.addEvent(AsyncPlayerChatEvent.class, AsyncPlayerChatEventCommand.class);
		eventListener.addEvent(ItemDespawnEvent.class, ItemDespawnEventCommand.class);
		eventListener.addEvent(PlayerPickupItemEvent.class, PlayerPickupItemEventCommand.class);
		eventListener.addEvent(BlockBreakEvent.class, BlockBreakEventCommand.class);
		eventListener.addEvent(PlayerInteractEvent.class, PlayerInteractEventCommand.class);
		eventListener.addEvent(BlockPlaceEvent.class, BlockPlaceEventCommand.class);
		
		Object[] enums = Util.getStaticFields(PermissionsType.class);
		String[] permissions = Arrays.copyOf(enums, enums.length, String[].class);
		for (String p : permissions) {
			permissionsManager.addPermission(p);
		}
		
		tickHandler.addTickCommand("bomb", BombTickCommand.class, 10);
		tickHandler.addTickCommand("electrify", ElectrifyTickCommand.class, 10);
		tickHandler.addTickCommand("haunt", HauntTickCommand.class, 20);
		tickHandler.addTickCommand("slowpoke", SlowpokeTickCommand.class, 60);
		tickHandler.addTickCommand("spin", SpinTickCommand.class, 1);
		tickHandler.addTickCommand("weakling", WeaklingTickCommand.class, 60);
		tickHandler.addTickCommand("burn", BurnTickCommand.class, 60);
		tickHandler.addTickCommand("spam", SpamTickCommand.class, 10);
		tickHandler.addTickCommand("starve", StarveTickCommand.class, 20);
		tickHandler.addTickCommand("hurt", HurtTickCommand.class, 20);
		tickHandler.addTickCommand("slowmine", SlowMineTickCommand.class, 60);
		tickHandler.addTickCommand("nausea", NauseaTickCommand.class, 200);
		tickHandler.addTickCommand("vegetable", VegetableTickCommand.class, 10);
		tickHandler.addTickCommand("alone", AloneTickCommand.class, 20);
		tickHandler.addTickCommand("annoy", AnnoyTickCommand.class, 20);
		tickHandler.addTickCommand("sparta", SpartaTickCommand.class, 10);
		tickHandler.addTickCommand("rewind", RewindTickCommand.class, 5);
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Enabled" + ChatColor.GREEN + " ==--");
	}
	public void onDisable() {
		commandHandler.clearCommands();
		eventListener.clearEvents();
		permissionsManager.clearPermissions();
		tickHandler.clearTickCommands();
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--== " + ChatColor.LIGHT_PURPLE + "TrollCommands++ Disabled" + ChatColor.GREEN + " ==--");
	}
	
	//private
	private ActionListener onUpdateTimer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			checkUpdate();
		}
	};
	private void checkUpdate() {
		Updater updater = new Updater(this, 100359, getFile(), UpdateType.NO_DOWNLOAD, false);
		if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "--== " + ChatColor.YELLOW + "TrollCommands++ UPDATE AVAILABLE" + ChatColor.GREEN + " ==--");
		}
	}
}