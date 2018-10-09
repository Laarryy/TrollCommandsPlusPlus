package me.egg82.tcpp.messages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.bukkit.Bukkit;

import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.async.AsyncMessageHandler;
import ninja.egg82.plugin.messaging.IMessageHandler;

public class TrollMessage extends AsyncMessageHandler {
    //vars
    private String senderId = ServiceLocator.getService(IMessageHandler.class).getSenderId();

    //constructor
    public TrollMessage() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (!channelName.equals("troll")) {
            return;
        }
        if (sender.equals(senderId)) {
            return;
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DataInputStream in = new DataInputStream(stream);

        String command = null;

        try {
            command = in.readUTF();
        } catch (Exception ex) {
            IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
            if (handler != null) {
                handler.sendException(ex);
            }
            throw new RuntimeException(ex);
        }

        String threadCommand = "troll " + command;

        TaskUtil.runSync(new Runnable() {
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), threadCommand);
            }
        });
    }
}
