package me.egg82.tcpp;

import java.util.Arrays;

import org.bukkit.event.entity.PlayerDeathEvent;

import com.egg82.patterns.ServiceLocator;
import com.egg82.plugin.BasePlugin;
import com.egg82.registry.Registry;
import com.egg82.utils.Util;

import me.egg82.tcpp.commands.BanishCommand;
import me.egg82.tcpp.commands.BombCommand;
import me.egg82.tcpp.commands.TrollCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.egg82.tcpp.events.PlayerDeathEventCommand;
import me.egg82.tcpp.ticks.BombTickCommand;

public class TrollCommandsPlusPlus extends BasePlugin {
	//vars
	
	//constructor
	public TrollCommandsPlusPlus() {
		
	}
	
	//public
	public void onLoad() {
		super.onLoad();
		
		ServiceLocator.provideService(PluginServiceType.BOMB_REGISTRY, Registry.class);
	}
	
	public void onEnable() {
		super.onEnable();
		
		commandHandler.addCommand("troll", TrollCommand.class);
		commandHandler.addCommand("banish", BanishCommand.class);
		commandHandler.addCommand("bomb", BombCommand.class);
		
		eventListener.addEvent(PlayerDeathEvent.class, PlayerDeathEventCommand.class);
		
		Object[] enums = Util.getStaticFields(PermissionsType.class);
		String[] permissions = Arrays.copyOf(enums, enums.length, String[].class);
		for (String p : permissions) {
			permissionsManager.addPermission(p);
		}
		
		tickHandler.addTickCommand("bomb", BombTickCommand.class, 10);
		
		getLogger().info("--== TrollCommands++ Enabled ==--");
	}
	public void onDisable() {
		commandHandler.clearCommands();
		eventListener.clearEvents();
		permissionsManager.clearPermissions();
		tickHandler.clearTickCommands();
		
		getLogger().info("--== TrollCommands++ Disabled ==--");
	}
	
	//private
	
}