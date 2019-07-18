package me.egg82.tcpp;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.RegisteredCommand;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SetMultimap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import me.egg82.tcpp.commands.TrollCommand;
import me.egg82.tcpp.events.PlayerLoginUpdateNotifyHandler;
import me.egg82.tcpp.events.entity.entityChangeBlock.EntityChangeBlockAnvil;
import me.egg82.tcpp.events.entity.entityDamageByEntity.EntityDamageByEntityBrittle;
import me.egg82.tcpp.events.entity.entityExplode.EntityExplodeBludger;
import me.egg82.tcpp.events.inventory.inventoryClick.InventoryClickAttach;
import me.egg82.tcpp.events.inventory.inventoryClick.InventoryClickAttachErase;
import me.egg82.tcpp.events.inventory.inventoryDrag.InventoryDragAttach;
import me.egg82.tcpp.events.inventory.inventoryDrag.InventoryDragAttachErase;
import me.egg82.tcpp.events.inventory.inventoryMoveItem.InventoryMoveItemAttach;
import me.egg82.tcpp.events.inventory.inventoryMoveItem.InventoryMoveItemAttachErase;
import me.egg82.tcpp.events.player.asyncPlayerChat.AsyncPlayerChatAlone;
import me.egg82.tcpp.events.player.asyncPlayerChat.AsyncPlayerChatAmnesia;
import me.egg82.tcpp.events.player.playerDeath.PlayerDeathBludger;
import me.egg82.tcpp.events.player.playerDeath.PlayerDeathBrittle;
import me.egg82.tcpp.events.player.playerDropItem.PlayerDropItemAttachErase;
import me.egg82.tcpp.events.player.playerJoin.PlayerJoinAlone;
import me.egg82.tcpp.events.player.playerMove.PlayerMoveBrittle;
import me.egg82.tcpp.events.player.playerPickupItem.PlayerPickupItemAttach;
import me.egg82.tcpp.events.player.playerQuit.PlayerQuitBludger;
import me.egg82.tcpp.extended.Configuration;
import me.egg82.tcpp.hooks.PlayerAnalyticsHook;
import me.egg82.tcpp.hooks.PluginHook;
import me.egg82.tcpp.hooks.ProtocolLibHook;
import me.egg82.tcpp.services.AnalyticsHelper;
import me.egg82.tcpp.services.GameAnalyticsErrorHandler;
import me.egg82.tcpp.services.block.FakeBlockHandler;
import me.egg82.tcpp.services.entity.EntityItemHandler;
import me.egg82.tcpp.services.player.PlayerVisibilityHandler;
import me.egg82.tcpp.tasks.TaskAnnoy;
import me.egg82.tcpp.tasks.TaskBludger;
import me.egg82.tcpp.tasks.TaskBomb;
import me.egg82.tcpp.utils.*;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEventSubscriber;
import ninja.egg82.events.BukkitEvents;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import ninja.egg82.updater.SpigotUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.help.HelpTopic;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrollCommandsPlusPlus {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService workPool = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setNameFormat("TrollCommandsPlusPlus-%d").build());

    private TaskChainFactory taskFactory;
    private PaperCommandManager commandManager;

    private List<BukkitEventSubscriber<?>> events = new ArrayList<>();
    private List<Integer> tasks = new ArrayList<>();

    private List<String> commandNames = new ArrayList<>();
    private static final Field commandMap;

    static {
        try {
            commandMap = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Could not get command map.", ex);
        }
    }

    private Metrics metrics = null;

    private final Plugin plugin;
    private final boolean isBukkit;

    public TrollCommandsPlusPlus(Plugin plugin) {
        isBukkit = BukkitEnvironmentUtil.getEnvironment() == BukkitEnvironmentUtil.Environment.BUKKIT;
        this.plugin = plugin;
    }

    public void onLoad() {
        if (BukkitEnvironmentUtil.getEnvironment() != BukkitEnvironmentUtil.Environment.PAPER) {
            log(Level.INFO, ChatColor.AQUA + "====================================");
            log(Level.INFO, ChatColor.YELLOW + "TrollCommands++ runs better on Paper!");
            log(Level.INFO, ChatColor.YELLOW + "https://whypaper.emc.gs/");
            log(Level.INFO, ChatColor.AQUA + "====================================");
        }

        if (BukkitVersionUtil.getGameVersion().startsWith("1.8")) {
            log(Level.INFO, ChatColor.AQUA + "====================================");
            log(Level.INFO, ChatColor.DARK_RED + "DEAR LORD why are you on 1.8???");
            log(Level.INFO, ChatColor.DARK_RED + "Have you tried ViaVersion or ProtocolSupport lately?");
            log(Level.INFO, ChatColor.AQUA + "====================================");
        }
    }

    public void onEnable() {
        GameAnalyticsErrorHandler.open(ServerIDUtil.getID(new File(plugin.getDataFolder(), "stats-id.txt")), plugin.getDescription().getVersion(), Bukkit.getVersion());

        taskFactory = BukkitTaskChainFactory.create(plugin);
        commandManager = new PaperCommandManager(plugin);
        commandManager.enableUnstableAPI("help");

        loadServices();
        loadCommands();
        loadEvents();
        loadTasks();
        loadHooks();
        loadMetrics();

        plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.GREEN + "Enabled");

        plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading()
                + ChatColor.YELLOW + "[" + ChatColor.AQUA + "Version " + ChatColor.WHITE + plugin.getDescription().getVersion() + ChatColor.YELLOW +  "] "
                + ChatColor.YELLOW + "[" + ChatColor.WHITE + commandManager.getRegisteredRootCommands().size() + ChatColor.GOLD + " Commands" + ChatColor.YELLOW +  "] "
                + ChatColor.YELLOW + "[" + ChatColor.WHITE + tasks.size() + ChatColor.GRAY + " Tasks" + ChatColor.YELLOW +  "] "
                + ChatColor.YELLOW + "[" + ChatColor.WHITE + events.size() + ChatColor.BLUE + " Events" + ChatColor.YELLOW +  "]"
        );

        workPool.submit(this::checkUpdate);
    }

    public void onDisable() {
        taskFactory.shutdown(8, TimeUnit.SECONDS);
        commandManager.unregisterCommands();

        for (int task : tasks) {
            Bukkit.getScheduler().cancelTask(task);
        }
        tasks.clear();

        for (BukkitEventSubscriber<?> event : events) {
            event.cancel();
        }
        events.clear();

        unloadHooks();
        unloadServices();

        plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "Disabled");

        GameAnalyticsErrorHandler.close();
    }

    private void loadServices() {
        ConfigurationFileUtil.reloadConfig(plugin);

        try {
            ServiceLocator.register(BukkitVersionUtil.getBestMatch(EntityItemHandler.class, BukkitVersionUtil.getGameVersion(), "me.egg82.tcpp.services.entity", false), false);
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }

        try {
            ServiceLocator.register(BukkitVersionUtil.getBestMatch(FakeBlockHandler.class, BukkitVersionUtil.getGameVersion(), "me.egg82.tcpp.services.block", false), false);
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }

        try {
            ServiceLocator.register(BukkitVersionUtil.getBestMatch(PlayerVisibilityHandler.class, BukkitVersionUtil.getGameVersion(), "me.egg82.tcpp.services.player", false), false);
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }

        ServiceLocator.register(new SpigotUpdater(plugin, 24237));
    }

    private void loadCommands() {
        commandManager.getCommandCompletions().registerCompletion("player", c -> {
            String lower = c.getInput().toLowerCase();
            Set<String> players = new LinkedHashSet<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (lower.isEmpty() || p.getName().toLowerCase().startsWith(lower)) {
                    Player player = c.getPlayer();
                    if (c.getSender().isOp() || (player != null && player.canSee(p) && !isVanished(p))) {
                        players.add(p.getName());
                    }
                }
            }
            return ImmutableList.copyOf(players);
        });

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Hacks, first tick doesn't execute until everything's loaded
            for (HelpTopic topic : Bukkit.getServer().getHelpMap().getHelpTopics()) {
                String name = topic.getName();
                if (name.charAt(0) != '/') {
                    continue;
                }

                commandNames.add(name.substring(1));
            }
        }, 1L);

        commandManager.getCommandCompletions().registerCompletion("topic", c -> {
            // Whew, alrighty, so THIS is a hack and a half.
            if (c.getContextValue(Object.class, 0) == null) {
                // We are at the first arg
                if (c.getInput().isEmpty()) {
                    return ImmutableList.copyOf(commandNames);
                }

                List<String> retVal = new ArrayList<>();
                String lower = c.getInput().toLowerCase();
                for (String command : commandNames) {
                    if (command.toLowerCase().startsWith(lower)) {
                        retVal.add(command);
                    }
                }
                return ImmutableList.copyOf(retVal);
            }

            // We are NOT at the first arg

            String[] args = c.getContextValue(String[].class, 1);
            try {
                CommandMap map = (CommandMap) commandMap.get(Bukkit.getPluginManager());
                List<String> completions = map.tabComplete(c.getSender(), String.join(" ", args));
                return completions != null ? ImmutableList.copyOf(completions) : ImmutableList.of();
            } catch (IllegalAccessException ex) {
                logger.error(ex.getMessage(), ex);
            }

            return ImmutableList.of();
        });

        commandManager.getCommandCompletions().registerCompletion("subcommand", c -> {
            String lower = c.getInput().toLowerCase();
            Set<String> commands = new LinkedHashSet<>();
            SetMultimap<String, RegisteredCommand> subcommands = commandManager.getRootCommand("trollcommandsplusplus").getSubCommands();
            for (Map.Entry<String, RegisteredCommand> kvp : subcommands.entries()) {
                if (!kvp.getValue().isPrivate() && (lower.isEmpty() || kvp.getKey().toLowerCase().startsWith(lower)) && kvp.getValue().getCommand().indexOf(' ') == -1) {
                    commands.add(kvp.getValue().getCommand());
                }
            }
            return ImmutableList.copyOf(commands);
        });

        commandManager.registerCommand(new TrollCommand(plugin, taskFactory));
    }

    private void loadEvents() {
        events.add(BukkitEvents.subscribe(plugin, PlayerLoginEvent.class, EventPriority.LOW).handler(e -> new PlayerLoginUpdateNotifyHandler(plugin).accept(e)));

        events.add(BukkitEvents.subscribe(plugin, PlayerJoinEvent.class, EventPriority.NORMAL).handler(e -> new PlayerJoinAlone(plugin).accept(e)));
        events.add(BukkitEvents.subscribe(plugin, AsyncPlayerChatEvent.class, EventPriority.LOW).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new AsyncPlayerChatAlone().accept(e)));

        events.add(BukkitEvents.subscribe(plugin, AsyncPlayerChatEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new AsyncPlayerChatAmnesia(plugin).accept(e)));
        events.add(BukkitEvents.subscribe(plugin, EntityChangeBlockEvent.class, EventPriority.NORMAL).handler(e -> new EntityChangeBlockAnvil().accept(e)));

        events.add(BukkitEvents.subscribe(plugin, InventoryClickEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new InventoryClickAttachErase(plugin).accept(e)));
        events.add(BukkitEvents.subscribe(plugin, InventoryDragEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new InventoryDragAttachErase(plugin).accept(e)));
        events.add(BukkitEvents.subscribe(plugin, InventoryMoveItemEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new InventoryMoveItemAttachErase().accept(e)));
        events.add(BukkitEvents.subscribe(plugin, PlayerDropItemEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new PlayerDropItemAttachErase().accept(e)));
        if (plugin.getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            events.add(BukkitEvents.subscribe(plugin, InventoryClickEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new InventoryClickAttach(plugin).accept(e)));
            events.add(BukkitEvents.subscribe(plugin, InventoryDragEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new InventoryDragAttach(plugin).accept(e)));
            events.add(BukkitEvents.subscribe(plugin, InventoryMoveItemEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new InventoryMoveItemAttach().accept(e)));
            events.add(BukkitEvents.subscribe(plugin, PlayerPickupItemEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new PlayerPickupItemAttach().accept(e)));
        }

        events.add(BukkitEvents.subscribe(plugin, EntityExplodeEvent.class, EventPriority.NORMAL).filter(BukkitEventFilters.ignoreCancelled()).handler(e -> new EntityExplodeBludger().accept(e)));
        events.add(BukkitEvents.subscribe(plugin, PlayerQuitEvent.class, EventPriority.NORMAL).handler(e -> new PlayerQuitBludger().accept(e)));
        events.add(BukkitEvents.subscribe(plugin, PlayerDeathEvent.class, EventPriority.NORMAL).handler(e -> new PlayerDeathBludger().accept(e)));

        events.add(BukkitEvents.subscribe(plugin, PlayerMoveEvent.class, EventPriority.NORMAL).handler(e -> new PlayerMoveBrittle().accept(e)));
        events.add(BukkitEvents.subscribe(plugin, EntityDamageByEntityEvent.class, EventPriority.NORMAL).filter(e -> e.getEntity() instanceof LivingEntity).handler(e -> new EntityDamageByEntityBrittle().accept(e)));
        events.add(BukkitEvents.subscribe(plugin, PlayerDeathEvent.class, EventPriority.NORMAL).handler(e -> new PlayerDeathBrittle().accept(e)));
    }

    private void loadTasks() {
        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TaskAnnoy(), 0L, 45L));
        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TaskBludger(), 0L, 3L));
        tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TaskBomb(), 0L, 10L));
    }

    private void loadHooks() {
        PluginManager manager = plugin.getServer().getPluginManager();

        if (manager.getPlugin("Plan") != null) {
            plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.GREEN + "Enabling support for Plan.");
            ServiceLocator.register(new PlayerAnalyticsHook());
        } else {
            plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.YELLOW + "Plan was not found. Personal analytics support has been disabled.");
        }

        if (manager.getPlugin("ProtocolLib") != null) {
            plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.GREEN + "Enabling support for ProtocolLib.");
            Set<? extends FakeBlockHandler> handlers = ServiceLocator.remove(FakeBlockHandler.class);
            for (FakeBlockHandler h : handlers) {
                h.removeAll();
            }
            ServiceLocator.register(new ProtocolLibHook(plugin));
        } else {
            plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.YELLOW + "ProtocolLib was not found. Falling back to vanilla mechanics.");
        }
    }

    private void loadMetrics() {
        metrics = new Metrics(plugin);

        metrics.addCustomChart(new Metrics.AdvancedPie("commands", AnalyticsHelper::getAndEraseCommands));
    }

    private void checkUpdate() {
        Optional<Configuration> config = ConfigUtil.getConfig();
        if (!config.isPresent()) {
            return;
        }

        SpigotUpdater updater;
        try {
            updater = ServiceLocator.get(SpigotUpdater.class);
        } catch (InstantiationException | IllegalAccessException | ServiceNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return;
        }

        if (!config.get().getNode("update", "check").getBoolean(true)) {
            return;
        }

        updater.isUpdateAvailable().thenAccept(v -> {
            if (!v) {
                return;
            }

            try {
                plugin.getServer().getConsoleSender().sendMessage(LogUtil.getHeading() + ChatColor.AQUA + " has an " + ChatColor.GREEN + "update" + ChatColor.AQUA + " available! New version: " + ChatColor.YELLOW + updater.getLatestVersion().get());
            } catch (ExecutionException ex) {
                logger.error(ex.getMessage(), ex);
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            }
        });

        try {
            Thread.sleep(60L * 60L * 1000L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        workPool.submit(this::checkUpdate);
    }

    private void unloadHooks() {
        Set<? extends PluginHook> hooks = ServiceLocator.remove(PluginHook.class);
        for (PluginHook hook : hooks) {
            hook.cancel();
        }
    }

    public void unloadServices() { }

    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    private void log(Level level, String message) {
        plugin.getServer().getLogger().log(level, (isBukkit) ? ChatColor.stripColor(message) : message);
    }
}
