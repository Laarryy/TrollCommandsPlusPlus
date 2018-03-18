package me.egg82.tcpp.messages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.bukkit.Bukkit;

import ninja.egg82.exceptionHandlers.IExceptionHandler;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.AsyncMessageCommand;
import ninja.egg82.plugin.handlers.IMessageHandler;
import ninja.egg82.plugin.utils.TaskUtil;

public class CommandMessage extends AsyncMessageCommand {
	//vars
	private String senderId = ServiceLocator.getService(IMessageHandler.class).getSenderId();
	
	//constructor
	public CommandMessage() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!channelName.equals("Troll")) {
			return;
		}
		if (sender.equals(senderId)) {
			return;
		}
		
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		DataInputStream in = new DataInputStream(stream);
		
		String command = null;
		
		try {
			command = in.readUTF();
		} catch (Exception ex) {
			ServiceLocator.getService(IExceptionHandler.class).silentException(ex);
			throw new RuntimeException(ex);
		}
		
		String threadCommand = command;
		
		TaskUtil.runSync(new Runnable() {
			public void run() {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), threadCommand);
			}
		});
	}
}
