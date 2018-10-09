package me.egg82.tcpp.ticks;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import me.egg82.tcpp.registries.RadiateRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.FlowerPot;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.material.MaterialData;

import ninja.egg82.bukkit.core.BlockData;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.reflection.material.IMaterialHelper;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;

public class RadiateTickCommand extends TickHandler {
    //vars
    private IVariableRegistry<UUID> radiationRegistry = ServiceLocator.getService(RadiateRegistry.class);

    private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
    private IMaterialHelper materialHelper = ServiceLocator.getService(IMaterialHelper.class);

    //constructor
    public RadiateTickCommand() {
        super(0L, 10L);
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        for (UUID key : radiationRegistry.getKeys()) {
            e(CommandUtil.getPlayerByUuid(key));
        }
    }

    private void e(Player player) {
        if (player == null) {
            return;
        }

        if (Math.random() <= 0.05d) {
            List<BlockData> blocks = BlockUtil.getBlocks(player.getLocation(), 3, 3, 3);
            Collections.shuffle(blocks);

            for (int i = 0; i < blocks.size(); i++) {
                Material mat = blocks.get(i).getType();
                String matString = mat.name().toLowerCase();
                Location loc = blocks.get(i).getLocation();

                if (mat == Material.GRASS) {
                    loc.getBlock().setType(Material.DIRT);
                    break;
                } else if (matString.contains("sapling")) {
                    loc.getBlock().setType(Material.DEAD_BUSH);
                    break;
                } else if (matString.contains("leaves")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (matString.contains("grass")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (matString.contains("flower") && !matString.contains("pot")) {
                    loc.getBlock().setType(Material.DEAD_BUSH);
                    break;
                } else if (mat == Material.FLOWER_POT) {
                    FlowerPot pot = (FlowerPot) blocks.get(i).getLocation().getBlock().getState();
                    pot.setContents(new MaterialData(Material.DEAD_BUSH));
                    pot.update(true, true);
                    break;
                } else if (matString.contains("mushroom")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == Material.WHEAT) {
                    loc.getBlock().setType(Material.DEAD_BUSH);
                    break;
                } else if (mat == Material.CACTUS) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == materialHelper.getByName("SUGAR_CANE_BLOCK")) {
                    loc.getBlock().setType(Material.DEAD_BUSH);
                    break;
                } else if (mat == Material.PUMPKIN) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == materialHelper.getByName("MELON_BLOCK")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == Material.PUMPKIN_STEM) {
                    loc.getBlock().setType(Material.DEAD_BUSH);
                    break;
                } else if (mat == Material.MELON_STEM) {
                    loc.getBlock().setType(Material.DEAD_BUSH);
                    break;
                } else if (mat == Material.VINE) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == materialHelper.getByName("MYCEL")) {
                    loc.getBlock().setType(Material.DIRT);
                    break;
                } else if (mat == materialHelper.getByName("WATER_LILY")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (matString.equals("nether_wart_block")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == Material.COCOA) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == Material.CARROT) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (mat == Material.POTATO) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (matString.contains("plant")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                } else if (matString.equals("beetroot_block")) {
                    loc.getBlock().setType(Material.AIR);
                    break;
                }
            }
        }

        if (Math.random() <= 0.05d) {
            List<Entity> entities = player.getNearbyEntities(3.0d, 3.0d, 3.0d);
            Collections.shuffle(entities);

            for (Entity e : entities) {
                if (!(e instanceof Animals)) {
                    continue;
                }

                entityUtil.damage((Damageable) e, DamageCause.POISON, 0.5d);
                break;
            }
        }
    }
}
