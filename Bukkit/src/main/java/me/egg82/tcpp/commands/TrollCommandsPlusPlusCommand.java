package me.egg82.tcpp.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import co.aikar.taskchain.TaskChainFactory;
import me.egg82.tcpp.commands.internal.ReloadCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@CommandAlias("trollcommandsplusplus|tcpp")
public class TrollCommandsPlusPlusCommand extends BaseCommand {
    private final Plugin plugin;
    private final TaskChainFactory taskFactory;

    public TrollCommandsPlusPlusCommand(Plugin plugin, TaskChainFactory taskFactory) {
        this.plugin = plugin;
        this.taskFactory = taskFactory;
    }

    @Subcommand("reload")
    @CommandPermission("tcpp.admin")
    @Description("{@@description.reload}")
    public void onReload(CommandIssuer issuer) { new ReloadCommand(plugin, taskFactory.newChain(), issuer).run(); }

    @CatchUnknown @Default
    @CommandCompletion("@subcommand")
    public void onDefault(CommandSender sender, String[] args) {
        Bukkit.getServer().dispatchCommand(sender, "trollcommandsplusplus help");
    }

    @HelpCommand
    @Syntax("[command]")
    public void onHelp(CommandSender sender, CommandHelp help) { help.showHelp(); }
}
