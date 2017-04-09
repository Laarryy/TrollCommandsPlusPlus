package me.egg82.tcpp.commands;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;

public class TrollCommand extends BasePluginCommand {
	//vars
	private MetricsHelper metricsHelper = (MetricsHelper) ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public TrollCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(false, PermissionsType.COMMAND_TROLL, new int[]{0,1}, null)) {
			if (args.length == 0) {
				sender.getServer().dispatchCommand(sender, "help trollcommandsplusplus");
			} else if (args.length == 1) {
				sender.getServer().dispatchCommand(sender, "help trollcommandsplusplus " + args[0]);
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
}