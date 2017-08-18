package me.egg82.tcpp.events.block.blockBreak;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.core.LuckyCommand;
import me.egg82.tcpp.services.LuckyBlockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class LuckyBlockEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private ArrayList<LuckyCommand> luckyCommands = new ArrayList<LuckyCommand>();
	private ArrayList<LuckyCommand> unluckyCommands = new ArrayList<LuckyCommand>();
	
	private IRegistry<Location> luckyBlockRegistry = ServiceLocator.getService(LuckyBlockRegistry.class);
	
	//constructor
	public LuckyBlockEventCommand(BlockBreakEvent event) {
		super(event);
		
		List<Class<? extends LuckyCommand>> commands = ReflectUtil.getClasses(LuckyCommand.class, "me.egg82.tcpp.commands.lucky");
		for (int i = 0; i < commands.size(); i++) {
			LuckyCommand run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(Player.class).newInstance(event.getPlayer());
			} catch (Exception ex) {
				continue;
			}
			luckyCommands.add(run);
		}
		
		commands = ReflectUtil.getClasses(LuckyCommand.class, "me.egg82.tcpp.commands.unlucky");
		for (int i = 0; i < commands.size(); i++) {
			LuckyCommand run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(Player.class).newInstance(event.getPlayer());
			} catch (Exception ex) {
				continue;
			}
			unluckyCommands.add(run);
		}
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Location loc = event.getBlock().getLocation();
		Double luck = luckyBlockRegistry.getRegister(loc, Double.class);
		
		if (luck == null) {
			return;
		}
		
		if (Math.random() <= luck) {
			LuckyCommand command = luckyCommands.get(MathUtil.fairRoundedRandom(0, luckyCommands.size() - 1));
			command.setPlayer(event.getPlayer());
			command.start();
		} else {
			LuckyCommand command = unluckyCommands.get(MathUtil.fairRoundedRandom(0, unluckyCommands.size() - 1));
			command.setPlayer(event.getPlayer());
			command.start();
		}
		
		luckyBlockRegistry.removeRegister(loc);
	}
}
