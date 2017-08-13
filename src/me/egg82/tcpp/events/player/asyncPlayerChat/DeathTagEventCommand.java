package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.services.DeathTagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class DeathTagEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry<UUID> deathTagRegistry = ServiceLocator.getService(DeathTagRegistry.class);
	
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public DeathTagEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (deathTagRegistry.hasRegister(uuid)) {
			String[] split = event.getMessage().split("\\s");
			for (String s : split) {
				Player p = CommandUtil.getPlayerByName(s, false);
				if (p != null) {
					if (CommandUtil.hasPermission(p, PermissionsType.IMMUNE)) {
						continue;
					}
					
					deathTagRegistry.removeRegister(uuid);
					deathTagRegistry.setRegister(p.getUniqueId(), null);
					
					TaskUtil.runSync(new Runnable() {
						public void run() {
							p.setHealth(0.0d);
							entityUtil.damage(p, EntityDamageEvent.DamageCause.SUICIDE, Double.MAX_VALUE);
						}
					}, 1L);
					return;
				}
			}
		}
	}
}
