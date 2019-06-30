package me.egg82.tcpp.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.taskchain.TaskChainFactory;
import me.egg82.tcpp.commands.internal.*;
import me.egg82.tcpp.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@CommandAlias("trollcommandsplusplus|troll|t")
public class TrollCommand extends BaseCommand {
    private final Plugin plugin;
    private final TaskChainFactory taskFactory;

    public TrollCommand(Plugin plugin, TaskChainFactory taskFactory) {
        this.plugin = plugin;
        this.taskFactory = taskFactory;
    }

    @Subcommand("reload")
    @CommandPermission("tcpp.admin")
    @Description("Reloads the plugin.")
    public void onReload(CommandSender sender) {
        new ReloadCommand(plugin, taskFactory.newChain(), sender).run();
    }

    @CatchUnknown @Default
    @CommandCompletion("@subcommand")
    public void onDefault(CommandSender sender, String[] args) {
        Bukkit.getServer().dispatchCommand(sender, "trollcommandsplusplus help");
    }

    @Subcommand("alone")
    @CommandPermission("tcpp.command.alone")
    @Description("(Toggleable) Hides all other players from the specified player.")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAlone(CommandSender sender, String playerName) {
        new AloneCommand(plugin, taskFactory.newChain(), sender, playerName).run();
    }

    @Subcommand("amnesia")
    @CommandPermission("tcpp.command.amnesia")
    @Description("(Toggleable) Screws with the player's chat in weird ways.")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAmnesia(CommandSender sender, String playerName) {
        new AmnesiaCommand(taskFactory.newChain(), sender, playerName).run();
    }

    @Subcommand("annoy")
    @CommandPermission("tcpp.command.annoy")
    @Description("(Toggleable) Hrmm. Hmm.. Hrrm.. Hrm. Hm. Hmm..")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAnnoy(CommandSender sender, String playerName) {
        new AnnoyCommand(taskFactory.newChain(), sender, playerName).run();
    }

    @Subcommand("anvil")
    @CommandPermission("tcpp.command.anvil")
    @Description("Now who could have put that up there?")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onAnvil(CommandSender sender, String playerName) {
        new AnvilCommand(plugin, taskFactory.newChain(), sender, playerName).run();
    }

    @Subcommand("attach")
    @CommandPermission("tcpp.command.attach")
    @Description("(Undoable) The specified command (topic) will be attached to an item. The next time this item is picked up, it will run the command as the payer that picked the item up..")
    @Syntax("[topic]")
    @CommandCompletion("@topic")
    public void onAttach(CommandSender sender, @Default() String[] topic) {
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            sender.sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "This command requires ProtocolLib!");
        }

        new AttachCommand(sender, topic).run();
    }

    @Subcommand("stop")
    @CommandPermission("tcpp.command.stop")
    @Description("Stops and undoes any currently-active trolls against the player.")
    @Syntax("<player>")
    @CommandCompletion("@player")
    public void onStop(CommandSender sender, String playerName) {
        new StopCommand(taskFactory.newChain(), sender, playerName).run();
    }

    @HelpCommand
    @Syntax("[command]")
    public void onHelp(CommandSender sender, CommandHelp help) { help.showHelp(); }
}
