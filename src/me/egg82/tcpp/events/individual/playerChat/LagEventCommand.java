package me.egg82.tcpp.events.individual.playerChat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand {
	//vars
	private IRegistry lagRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAG_REGISTRY);
	
	private Timer timer = null;
	private Object lock = new Object();
	
	//constructor
	public LagEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (lagRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			timer = new Timer(MathUtil.fairRoundedRandom(2000, 3000), onTimer);
			timer.setRepeats(false);
			timer.start();
			
			synchronized(lock) {
				try {
					lock.wait();
				} catch (Exception ex) {
					
				}
			}
		}
	}
	
	private ActionListener onTimer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			synchronized(lock) {
				lock.notifyAll();
			}
		}
	};
}
