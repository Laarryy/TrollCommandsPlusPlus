package me.egg82.tcpp.commands.internal;

import co.aikar.commands.CommandIssuer;
import co.aikar.taskchain.TaskChain;
import me.egg82.tcpp.enums.Message;
import me.egg82.tcpp.utils.ConfigurationFileUtil;
import org.bukkit.plugin.Plugin;

public class ReloadCommand implements Runnable {
    private final Plugin plugin;
    private final TaskChain<?> chain;
    private final CommandIssuer issuer;

    public ReloadCommand(Plugin plugin, TaskChain<?> chain, CommandIssuer issuer) {
        this.plugin = plugin;
        this.chain = chain;
        this.issuer = issuer;
    }

    public void run() {
        issuer.sendInfo(Message.RELOAD__BEGIN);

        chain
                .async(() -> ConfigurationFileUtil.reloadConfig(plugin))
                .sync(() -> issuer.sendInfo(Message.RELOAD__END))
                .execute();
    }
}
