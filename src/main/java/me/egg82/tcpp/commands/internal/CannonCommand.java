package me.egg82.tcpp.commands.internal;

import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

public class CannonCommand extends BaseCommand {
    private final double speed;
    private final double power;

    public CannonCommand(CommandSender sender, double speed, double power) {
        super(sender);
        this.speed = speed;
        this.power = power;
    }

    public void run() {
        if (!(sender instanceof Entity)) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "You cannot run this command as console.");
            return;
        }

        TNTPrimed tnt = ((Entity) sender).getLocation().getWorld().spawn(((Entity) sender).getLocation(), TNTPrimed.class);
        tnt.setYield((float) power);
        tnt.setIsIncendiary(power > 0.0d);
        Vector direction = ((Entity) sender).getLocation().getDirection().multiply(speed);
        tnt.setVelocity(direction);

        AnalyticsHelper.incrementCommand("cannon");
    }
}
