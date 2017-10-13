package me.egg82.tcpp.commands.unlucky;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.core.LuckyCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;
import ninja.egg82.utils.ReflectUtil;

public class SpawnBehindCommand extends LuckyCommand {
	//vars
	private EntityType[] mobTypes = null;
	
	//constructor
	public SpawnBehindCommand(Player player) {
		super(player);
		
		EntityType[] types = EntityType.values();
		ArrayList<EntityType> outTypes = new ArrayList<EntityType>();
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			if (!ReflectUtil.doesExtend(Monster.class, types[i].getEntityClass())) {
				continue;
			}
			
			outTypes.add(types[i]);
		}
		mobTypes = outTypes.toArray(new EntityType[0]);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Location spawnLocation = BlockUtil.getTopWalkableBlock(LocationUtil.getLocationBehind(player.getLocation(), 3.0d));
		int numMobs = MathUtil.fairRoundedRandom(4, 8);
		
		for (int i = 0; i < numMobs; i++) {
			EntityType spawned = mobTypes[MathUtil.fairRoundedRandom(0, mobTypes.length - 1)];
			
			Monster m = (Monster) player.getWorld().spawn(spawnLocation, spawned.getEntityClass());
			if (m instanceof PigZombie) {
				((PigZombie) m).setAngry(true);
			}
			m.setTarget(player);
			
			Vector v = player.getLocation().toVector().subtract(spawnLocation.toVector()).normalize().multiply(0.23d);
			if (LocationUtil.isFinite(v)) {
				m.setVelocity(v);
			}
		}
	}
}
