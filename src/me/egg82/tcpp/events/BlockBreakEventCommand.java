package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.block.BlockBreakEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class BlockBreakEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private ArrayList<EventCommand<BlockBreakEvent>> events = new ArrayList<EventCommand<BlockBreakEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BlockBreakEventCommand(BlockBreakEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.block.blockBreak");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<BlockBreakEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(BlockBreakEvent.class).newInstance(event);
			} catch (Exception ex) {
				continue;
			}
			events.add(run);
		}
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		events.forEach((v) -> {
			v.setEvent(event);
			v.start();
		});
	}
}
