package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.registry.interfaces.IRegistry;
import ninja.egg82.utils.MathUtil;

public class ClumsyTickCommand extends TickCommand {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.CLUMSY_REGISTRY);
	
	//constructor
	public ClumsyTickCommand() {
		super();
		ticks = 10l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = reg.registryNames();
		for (String name : names) {
			e((Player) reg.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.1) {
			PlayerInventory inv = player.getInventory();
			ItemStack[] items = inv.getContents();
			boolean isEmpty = true;
			
			for (ItemStack i : items) {
				if (i != null) {
					isEmpty = false;
					break;
				}
			}
			
			if (!isEmpty) {
				int droppedItemNum = 0;
				
				do {
					droppedItemNum = MathUtil.fairRoundedRandom(0, items.length - 1);
				} while (items[droppedItemNum] == null);
				
				ItemStack droppedItem = new ItemStack(items[droppedItemNum]);
				droppedItem.setAmount(1);
				
				if (items[droppedItemNum].getAmount() == 0) {
					items[droppedItemNum] = null;
				} else {
					items[droppedItemNum].setAmount(items[droppedItemNum].getAmount() - 1);
				}
				
				inv.setContents(items);
				player.getWorld().dropItemNaturally(BlockUtil.getTopAirBlock(player.getLocation().clone().add(MathUtil.random(2.0d, 3.0d) * ((Math.random() <= 0.5) ? -1 : 1), 0.0d, MathUtil.random(2.0d, 3.0d) * ((Math.random() <= 0.5) ? -1 : 1))), droppedItem);
			}
		}
	}
}
