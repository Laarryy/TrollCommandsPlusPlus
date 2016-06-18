package me.egg82.tcpp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mcstats.Metrics;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.events.AsyncPlayerChatEventCommand;
import me.egg82.tcpp.events.BlockBreakEventCommand;
import me.egg82.tcpp.events.BlockPlaceEventCommand;
import me.egg82.tcpp.events.EntityDamageEventCommand;
import me.egg82.tcpp.events.ItemDespawnEventCommand;
import me.egg82.tcpp.events.PlayerDeathEventCommand;
import me.egg82.tcpp.events.PlayerInteractEventCommand;
import me.egg82.tcpp.events.PlayerKickEventCommand;
import me.egg82.tcpp.events.PlayerLoginEventCommand;
import me.egg82.tcpp.events.PlayerMoveEventCommand;
import me.egg82.tcpp.events.PlayerPickupItemEventCommand;
import me.egg82.tcpp.events.PlayerQuitEventCommand;
import me.egg82.tcpp.ticks.AloneTickCommand;
import me.egg82.tcpp.ticks.AmnesiaTickCommand;
import me.egg82.tcpp.ticks.AnnoyTickCommand;
import me.egg82.tcpp.ticks.BombTickCommand;
import me.egg82.tcpp.ticks.BurnTickCommand;
import me.egg82.tcpp.ticks.ClumsyTickCommand;
import me.egg82.tcpp.ticks.ElectrifyTickCommand;
import me.egg82.tcpp.ticks.HauntTickCommand;
import me.egg82.tcpp.ticks.HurtTickCommand;
import me.egg82.tcpp.ticks.NauseaTickCommand;
import me.egg82.tcpp.ticks.PopupTickCommand;
import me.egg82.tcpp.ticks.RewindTickCommand;
import me.egg82.tcpp.ticks.SlowMineTickCommand;
import me.egg82.tcpp.ticks.SlowUndoTickCommand;
import me.egg82.tcpp.ticks.SlowpokeTickCommand;
import me.egg82.tcpp.ticks.SpamTickCommand;
import me.egg82.tcpp.ticks.SpartaTickCommand;
import me.egg82.tcpp.ticks.SpinTickCommand;
import me.egg82.tcpp.ticks.SquidTickCommand;
import me.egg82.tcpp.ticks.StarveTickCommand;
import me.egg82.tcpp.ticks.VegetableTickCommand;
import me.egg82.tcpp.ticks.WeaklingTickCommand;
import me.egg82.tcpp.ticks.WhoAmITickCommand;
import me.egg82.tcpp.util.RegistryUtil;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;
import net.gravitydevelopment.updater.Updater.UpdateType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.BasePlugin;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.registry.Registry;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.Util;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	private Timer updateTimer = null;
	
	private String commandsPackage = "me.egg82.tcpp.commands";
	
	//constructor
	public TrollCommandsPlusPlus() {
		super();
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		Object[] enums = null;
		
		enums = Util.getStaticFields(PluginServiceType.class);
		String[] services = Arrays.copyOf(enums, enums.length, String[].class);
		for (String s : services) {
			ServiceLocator.provideService(s, Registry.class);
		}
		
		IRegistry soundsRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SOUNDS_REGISTRY);
		enums = Util.getStaticFields(Sound.class);
		Sound[] sounds = Arrays.copyOf(enums, enums.length, Sound[].class);
		ArrayList<Sound> villagerSounds = new ArrayList<Sound>();
		for (Sound s : sounds) {
			String name = s.toString().toLowerCase();
			if (name.contains("villager") && !name.contains("zombie")) {
				villagerSounds.add(s);
			}
		}
		soundsRegistry.setRegister("all", sounds);
		soundsRegistry.setRegister("villager", villagerSounds.toArray(new Sound[0]));
		
		RegistryUtil.intialize();
		
		updateTimer = new Timer(24 * 60 * 60 * 1000, onUpdateTimer);
	}
	
	public void onEnable() {
		super.onEnable();
		
		try {
			Metrics m = new Metrics(this);
			m.start();
		} catch (Exception ex) {
			
		}
		
		checkUpdate();
		updateTimer.setRepeats(true);
		updateTimer.start();
		
		ArrayList<Class<? extends PluginCommand>> enums = Util.getClasses(PluginCommand.class, commandsPackage);
		for (Class<? extends PluginCommand> c : enums) {
			String name = c.getSimpleName();
			String pkg = c.getName();
			pkg = pkg.substring(0, pkg.lastIndexOf('.'));
			
			if (!pkg.equalsIgnoreCase(commandsPackage)) {
				continue;
			}
			
			commandHandler.addCommand(name.substring(0, name.length() - 7).toLowerCase(), c);
		}
		
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
		eventListener.addEvent(PlayerLoginEvent.class, PlayerLoginEventCommand.class);
		eventListener.addEvent(EntityDamageEvent.class, EntityDamageEventCommand.class);
		
		Object[] enums2 = Util.getStaticFields(PermissionsType.class);
		String[] permissions = Arrays.copyOf(enums2, enums2.length, String[].class);
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
		tickHandler.addTickCommand("popup", PopupTickCommand.class, 20);
		tickHandler.addTickCommand("squid", SquidTickCommand.class, 10);
		tickHandler.addTickCommand("whoami", WhoAmITickCommand.class, 20);
		tickHandler.addTickCommand("clumsy", ClumsyTickCommand.class, 10);
		tickHandler.addTickCommand("amnesia", AmnesiaTickCommand.class, 20);
		tickHandler.addTickCommand("slowUndo", SlowUndoTickCommand.class, 10);
		
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