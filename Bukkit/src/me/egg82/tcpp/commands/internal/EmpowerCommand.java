package me.egg82.tcpp.commands.internal;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.registries.EmpowerRegistry;
import me.egg82.tcpp.util.MetricsHelper;
import ninja.egg82.events.CompleteEventArgs;
import ninja.egg82.events.ExceptionEventArgs;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.PluginCommand;
import ninja.egg82.plugin.enums.SpigotLanguageType;
import ninja.egg82.plugin.exceptions.IncorrectCommandUsageException;
import ninja.egg82.plugin.exceptions.InvalidPermissionsException;
import ninja.egg82.plugin.exceptions.SenderNotAllowedException;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.LanguageUtil;

public class EmpowerCommand extends PluginCommand {
	//vars
	private IVariableRegistry<UUID> empowerRegistry = ServiceLocator.getService(EmpowerRegistry.class);
	
	private MetricsHelper metricsHelper = ServiceLocator.getService(MetricsHelper.class);
	
	//constructor
	public EmpowerCommand() {
		super();
	}
	
	//public
	public List<String> tabComplete() {
		return null;
	}
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (!CommandUtil.hasPermission(sender, PermissionsType.COMMAND_EMPOWER)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INVALID_PERMISSIONS));
			onError().invoke(this, new ExceptionEventArgs<InvalidPermissionsException>(new InvalidPermissionsException(sender, PermissionsType.COMMAND_EMPOWER)));
			return;
		}
		if (!CommandUtil.isPlayer(sender)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.SENDER_NOT_ALLOWED));
			onError().invoke(this, new ExceptionEventArgs<SenderNotAllowedException>(new SenderNotAllowedException(sender, this)));
			return;
		}
		if (!CommandUtil.isArrayOfAllowedLength(args, 0)) {
			sender.sendMessage(LanguageUtil.getString(SpigotLanguageType.INCORRECT_COMMAND_USAGE));
			String name = getClass().getSimpleName();
			name = name.substring(0, name.length() - 7).toLowerCase();
			sender.getServer().dispatchCommand(sender, "troll help " + name);
			onError().invoke(this, new ExceptionEventArgs<IncorrectCommandUsageException>(new IncorrectCommandUsageException(sender, this, args)));
			return;
		}
		
		e(((Player) sender).getUniqueId());
		
		onComplete().invoke(this, CompleteEventArgs.EMPTY);
	}
	private void e(UUID uuid) {
		if (empowerRegistry.hasRegister(uuid)) {
			empowerRegistry.removeRegister(uuid);
			
			sender.sendMessage("You will no longer empower (or disempower) the next player or mob you right-click.");
		} else {
			empowerRegistry.setRegister(uuid, null);
			metricsHelper.commandWasRun(this);
			
			sender.sendMessage("You will now empower (or disempower) the next player or mob you right-click!");
		}
	}
	
	protected void onUndo() {
		
	}
}