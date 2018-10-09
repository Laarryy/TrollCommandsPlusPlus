package me.egg82.tcpp.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import ninja.egg82.analytics.exceptions.IExceptionHandler;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.messaging.IMessageHandler;
import ninja.egg82.plugin.utils.ChannelUtil;

public class TrollChannelUtil {
    //vars

    //constructor
    public TrollChannelUtil() {

    }

    //public
    public static void broadcastTroll(String args) {
        if (args == null) {
            throw new IllegalArgumentException("args cannot be null.");
        }

        if (ServiceLocator.getService(IMessageHandler.class) == null) {
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(args);
        } catch (Exception ex) {
            IExceptionHandler handler = ServiceLocator.getService(IExceptionHandler.class);
            if (handler != null) {
                handler.sendException(ex);
            }
            ex.printStackTrace();
            return;
        }

        ChannelUtil.broadcastToServers("troll", stream.toByteArray());
    }

    //private

}
