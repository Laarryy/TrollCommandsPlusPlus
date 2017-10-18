package me.egg82.tcpp.messages;

import java.nio.charset.Charset;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.MessageCommand;
import ninja.egg82.plugin.handlers.CommandHandler;

public class CommandMessageCommand extends MessageCommand {
	//vars
	private final Charset UTF_8 = Charset.forName("UTF-8");
	
	private CommandHandler commandHandler = ServiceLocator.getService(CommandHandler.class);
	
	//constructor
	public CommandMessageCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!channelName.equals("tcpp_command")) {
			return;
		}
		
		String chat = new String(data, UTF_8);
		int space = chat.indexOf(' ');
		
		if (space == -1) {
			return;
		}
		
		String command = chat.substring(1, space);
		if (!commandHandler.hasCommand(command)) {
			return;
		}
		player.chat(chat);
	}
}
