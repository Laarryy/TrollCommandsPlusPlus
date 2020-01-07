package me.egg82.ae.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import co.aikar.taskchain.TaskChainFactory;
import me.egg82.ae.commands.internal.ReloadCommand;
import me.egg82.ae.commands.internal.RemoveCommand;
import me.egg82.ae.commands.internal.SetCommand;
import me.egg82.ae.commands.internal.SoulsCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@CommandAlias("advancedenchantments|ae")
public class AdvancedEnchantmentsCommand extends BaseCommand {
    private final Plugin plugin;
    private final TaskChainFactory taskFactory;

    public AdvancedEnchantmentsCommand(Plugin plugin, TaskChainFactory taskFactory) {
        this.plugin = plugin;
        this.taskFactory = taskFactory;
    }

    @Subcommand("reload")
    @CommandPermission("ae.admin")
    @Description("{@@description.reload}")
    public void onReload(CommandIssuer issuer) { new ReloadCommand(plugin, taskFactory.newChain(), issuer).run(); }

    @Subcommand("add|set|enchant")
    @CommandPermission("ae.admin")
    @Description("{@@description.set}")
    @Syntax("<enchant> [level] [force]")
    @CommandCompletion("@enchant @nothing @nothing")
    public void onSet(CommandIssuer issuer, String enchant, @Optional String level, @Default("false") String force) {
        new SetCommand(issuer, enchant, level, force).run();
    }

    @Subcommand("remove|delete")
    @CommandPermission("ae.admin")
    @Description("{@@description.remove}")
    @Syntax("<enchant>")
    @CommandCompletion("@enchant")
    public void onRemove(CommandIssuer issuer, String enchant) { new RemoveCommand(issuer, enchant).run(); }

    @Subcommand("souls|soul")
    @CommandPermission("ae.admin")
    @Description("{@@description.souls}")
    @Syntax("<num>")
    public void onSouls(CommandIssuer issuer, int souls) { new SoulsCommand(issuer, souls).run(); }

    @CatchUnknown @Default
    @CommandCompletion("@subcommand")
    public void onDefault(CommandSender sender, String[] args) {
        Bukkit.getServer().dispatchCommand(sender, "advancedenchantments help");
    }

    @HelpCommand
    @Syntax("[command]")
    public void onHelp(CommandSender sender, CommandHelp help) { help.showHelp(); }
}
