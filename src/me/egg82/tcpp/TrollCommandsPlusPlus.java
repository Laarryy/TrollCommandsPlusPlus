package me.egg82.tcpp;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcstats.Metrics;

import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.BasePlugin;
import com.egg82.registry.Registry;
import com.egg82.utils.Util;

import me.egg82.tcpp.commands.BanishCommand;
import me.egg82.tcpp.commands.BombCommand;
import me.egg82.tcpp.commands.CannonCommand;
import me.egg82.tcpp.commands.CometCommand;
import me.egg82.tcpp.commands.CreepCommand;
import me.egg82.tcpp.commands.ElectrifyCommand;
import me.egg82.tcpp.commands.EntombCommand;
import me.egg82.tcpp.commands.FreezeCommand;
import me.egg82.tcpp.commands.GarbleCommand;
import me.egg82.tcpp.commands.HauntCommand;
import me.egg82.tcpp.commands.LiftCommand;
import me.egg82.tcpp.commands.LureCommand;
import me.egg82.tcpp.commands.SlapCommand;
import me.egg82.tcpp.commands.SlowpokeCommand;
import me.egg82.tcpp.commands.SpinCommand;
import me.egg82.tcpp.commands.StampedeCommand;
import me.egg82.tcpp.commands.SwapCommand;
import me.egg82.tcpp.commands.TrollCommand;
import me.egg82.tcpp.commands.VaporizeCommand;
import me.egg82.tcpp.commands.WeaklingCommand;
import me.egg82.tcpp.commands.ZombifyCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.events.AsyncPlayerChatEventCommand;
import me.egg82.tcpp.events.PlayerDeathEventCommand;
import me.egg82.tcpp.events.PlayerKickEventCommand;
import me.egg82.tcpp.events.PlayerMoveEventCommand;
import me.egg82.tcpp.events.PlayerQuitEventCommand;
import me.egg82.tcpp.ticks.BombTickCommand;
import me.egg82.tcpp.ticks.ElectrifyTickCommand;
import me.egg82.tcpp.ticks.HauntTickCommand;
import me.egg82.tcpp.ticks.SlowpokeTickCommand;
import me.egg82.tcpp.ticks.SpinTickCommand;
import me.egg82.tcpp.ticks.WeaklingTickCommand;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	
	//constructor
	public TrollCommandsPlusPlus() {
		
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		ServiceLocator.provideService(PluginServiceType.BOMB_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.ELECTRIFY_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.FREEZE_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.GARBLE_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.HAUNT_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.SLOWPOKE_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.SPIN_REGISTRY, Registry.class);
		ServiceLocator.provideService(PluginServiceType.WEAKLING_REGISTRY, Registry.class);
	}
	
	public void onEnable() {
		super.onEnable();
		
		try {
			Metrics m = new Metrics(this);
			m.start();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
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
		
		eventListener.addEvent(PlayerDeathEvent.class, PlayerDeathEventCommand.class);
		eventListener.addEvent(PlayerQuitEvent.class, PlayerQuitEventCommand.class);
		eventListener.addEvent(PlayerKickEvent.class, PlayerKickEventCommand.class);
		eventListener.addEvent(PlayerMoveEvent.class, PlayerMoveEventCommand.class);
		eventListener.addEvent(AsyncPlayerChatEvent.class, AsyncPlayerChatEventCommand.class);
		
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
	
}