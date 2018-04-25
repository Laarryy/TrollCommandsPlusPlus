package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.RewindRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class RewindTickCommand extends TickCommand {
	//vars
	private IVariableRegistry<UUID> rewindRegistry = ServiceLocator.getService(RewindRegistry.class);
	
	//constructor
	public RewindTickCommand() {
		super(5L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : rewindRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		player.setPlayerTime(player.getPlayerTime() - 100L, false);
	}
}
