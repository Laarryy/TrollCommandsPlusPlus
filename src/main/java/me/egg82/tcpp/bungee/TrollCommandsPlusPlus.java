package me.egg82.tcpp.bungee;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import ninja.egg82.analytics.exceptions.GameAnalyticsExceptionHandler;
import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.analytics.exceptions.RollbarExceptionHandler;
import ninja.egg82.bungeecord.BasePlugin;
import ninja.egg82.bungeecord.processors.CommandProcessor;
import ninja.egg82.bungeecord.processors.EventProcessor;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.utils.PluginReflectUtil;
import ninja.egg82.utils.ThreadUtil;

public class TrollCommandsPlusPlus extends BasePlugin {
    //vars
    private int numCommands = 0;
    private int numEvents = 0;
    private int numMessages = 0;

    private IExceptionHandler exceptionHandler = null;
    private String version = null;

    //constructor
    public TrollCommandsPlusPlus() {
        super();
    }

    //public
    public void onLoad() {
        super.onLoad();

        version = getDescription().getVersion();

        exceptionHandler = ServiceLocator.getService(IExceptionHandler.class);
        getLogger().setLevel(Level.WARNING);

        PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.bungee.registries", true);
        PluginReflectUtil.addServicesFromPackage("me.egg82.tcpp.bungee.lists", true);
    }

    public void onEnable() {
        super.onEnable();

        swapExceptionHandlers(new RollbarExceptionHandler("78062d4e18074560850d4d8e0805b564", "production", version, getServerId(), getDescription().getName()));

        List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
        for (IMessageHandler handler : services) {
            try {
                handler.close();
            } catch (Exception ex) {

            }
        }

        Loaders.loadMessaging(getDescription().getName(), null, getServerId());

        numCommands = ServiceLocator.getService(CommandProcessor.class).addHandlersFromPackage("me.egg82.tcpp.bungee.commands", PluginReflectUtil.getCommandMapFromPackage("me.egg82.tcpp.bungee.commands", false, null, "Command"), false);
        numEvents = ServiceLocator.getService(EventProcessor.class).addHandlersFromPackage("me.egg82.tcpp.bungee.events");
        numMessages = ServiceLocator.getService(IMessageHandler.class).addHandlersFromPackage("me.egg82.tcpp.bungee.messages");

        enableMessage();

        ThreadUtil.rename(getDescription().getName());
        ThreadUtil.schedule(checkExceptionLimitReached, 60L * 60L * 1000L);
    }

    public void onDisable() {
        super.onDisable();

        ThreadUtil.shutdown(1000L);

        List<IMessageHandler> services = ServiceLocator.removeServices(IMessageHandler.class);
        for (IMessageHandler handler : services) {
            try {
                handler.close();
            } catch (Exception ex) {

            }
        }

        ServiceLocator.getService(CommandProcessor.class).clear();
        ServiceLocator.getService(EventProcessor.class).clear();

        exceptionHandler.close();

        disableMessage();
    }

    //private
    private Runnable checkExceptionLimitReached = new Runnable() {
        public void run() {
            if (exceptionHandler.isLimitReached()) {
                swapExceptionHandlers(new GameAnalyticsExceptionHandler("250e5c508c3dd844ed1f8bd2a449d1a6", "dfb50b06e598e7a7ad9b3c84f7b118c12800ffce", version, getServerId(), getDescription().getName()));
            }

            if (exceptionHandler.hasLimit()) {
                ThreadUtil.schedule(checkExceptionLimitReached, 10L * 60L * 1000L);
            }
        }
    };

    private void swapExceptionHandlers(IExceptionHandler newHandler) {
        List<IExceptionHandler> oldHandlers = ServiceLocator.removeServices(IExceptionHandler.class);

        exceptionHandler = newHandler;
        ServiceLocator.provideService(exceptionHandler);

        Logger logger = getLogger();
        if (exceptionHandler instanceof Handler) {
            logger.addHandler((Handler) exceptionHandler);
        }

        for (IExceptionHandler handler : oldHandlers) {
            if (handler instanceof Handler) {
                logger.removeHandler((Handler) handler);
            }

            handler.close();
            exceptionHandler.addLogs(handler.getUnsentLogs());
        }
    }

    private void enableMessage() {
        printInfo(ChatColor.GREEN + "Enabled.");
        printInfo(ChatColor.AQUA + "[Version " + getDescription().getVersion() + "] " + ChatColor.DARK_GREEN + numCommands + " commands " + ChatColor.LIGHT_PURPLE + numEvents + " events " + ChatColor.BLUE + numMessages + " message handlers");
    }

    private void disableMessage() {
        printInfo(ChatColor.RED + "Disabled");
    }
}
