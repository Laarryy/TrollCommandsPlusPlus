package me.egg82.tcpp.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.commands.base.BasePluginCommand;
import me.egg82.tcpp.enums.CommandErrorType;
import me.egg82.tcpp.enums.MessageType;
import me.egg82.tcpp.enums.PermissionsType;
import me.egg82.tcpp.enums.PluginServiceType;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import ninja.egg82.events.patterns.command.CommandEvent;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.registry.interfaces.IRegistry;

public class ControlCommand extends BasePluginCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_REGISTRY);
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROLLER_REGISTRY);
	private IRegistry reg3 = (IRegistry) ServiceLocator.getService(PluginServiceType.CONTROL_DATA_REGISTRY);
	
	//constructor
	public ControlCommand(CommandSender sender, Command command, String label, String[] args) {
		super(sender, command, label, args);
	}
	
	//public
	
	//private
	protected void execute() {
		if (isValid(true, PermissionsType.COMMAND_CONTROL, new int[]{0,1}, new int[]{0})) {
			Player p = (Player) sender;
			Player player = null;
			
			if (args.length == 0) {
				if (reg2.contains(p.getName().toLowerCase())) {
					player = (Player) reg2.getRegister(p.getName());
					if (player != null) {
						e(p.getName(), p, player.getName(), player);
					} else {
						e(p.getName(), p, "Anyone", null);
					}
				} else {
					sender.sendMessage(MessageType.NOT_CONTROLLING);
					dispatch(CommandEvent.ERROR, CommandErrorType.NOT_CONTROLLING);
					return;
				}
			} else if (args.length == 1) {
				player = Bukkit.getPlayer(args[0]);
				if (p.getName().toLowerCase() == player.getName().toLowerCase()) {
					sender.sendMessage(MessageType.NO_CONTROL_SELF);
					dispatch(CommandEvent.ERROR, CommandErrorType.NO_CONTROL_SELF);
					return;
				}
				e(p.getName(), p, player.getName(), player);
			}
			
			dispatch(CommandEvent.COMPLETE, null);
		}
	}
	@SuppressWarnings("unchecked")
	private void e(String controllerName, Player controller, String name, Player player) {
		PlayerInventory controllerInv = controller.getInventory();
		PlayerInventory playerInv = null;
		
		if (player != null) {
			playerInv = player.getInventory();
		}
		
		if (reg2.contains(controllerName.toLowerCase())) {
			sender.sendMessage("You are no longer controlling " + name + ".");
			if (player != null) {
				player.sendMessage("You are no longer being controlled.");
			}
			
			controller.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 3), true);
			
			if (playerInv != null) {
				playerInv.setContents(controllerInv.getContents());
			}
			ImmutableMap<String, Object> map = (ImmutableMap<String, Object>) reg3.getRegister(name);
			controllerInv.setContents((ItemStack[]) map.get("inventory"));
			if (player != null) {
				player.setGameMode((GameMode) map.get("mode"));
				player.teleport(controller);
			}
			
			DisguiseAPI.undisguiseToAll(controller);
			
			reg.setRegister(name.toLowerCase(), null);
			reg2.setRegister(controllerName.toLowerCase(), null);
			reg3.setRegister(name.toLowerCase(), null);
		} else {
			sender.sendMessage("You are now controlling " + name + "!");
			player.sendMessage("You are now being controlled!");
			
			reg.setRegister(name.toLowerCase(), player);
			reg2.setRegister(controllerName.toLowerCase(), player);
			reg3.setRegister(name.toLowerCase(), ImmutableMap.of("inventory", controllerInv.getContents(), "mode", player.getGameMode()));
			
			controllerInv.setContents(playerInv.getContents());
			controller.teleport(player);
			player.setGameMode(GameMode.SPECTATOR);
			
			DisguiseAPI.disguiseToAll(controller, new PlayerDisguise(name));
		}
	}
}
