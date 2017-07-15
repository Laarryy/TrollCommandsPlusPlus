package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.PotionSplashEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PotionSplashEventCommand extends EventCommand<PotionSplashEvent> {
	//vars
	private ArrayList<EventCommand<PotionSplashEvent>> events = new ArrayList<EventCommand<PotionSplashEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PotionSplashEventCommand(PotionSplashEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.potionSplash");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PotionSplashEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PotionSplashEvent.class).newInstance(event);
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
