package me.egg82.tcpp.commands.unlucky;

import org.bukkit.Material;

import me.egg82.tcpp.core.LuckyCommand;
import ninja.egg82.bukkit.reflection.material.IMaterialHelper;
import ninja.egg82.patterns.ServiceLocator;

public class LavaCommand extends LuckyCommand {
    //vars
    private Material web = ServiceLocator.getService(IMaterialHelper.class).getByName("WEB");

    //constructor
    public LavaCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        player.getWorld().getBlockAt(player.getEyeLocation()).setType(Material.LAVA);
        player.getWorld().getBlockAt(player.getLocation()).setType(web);
    }
}
