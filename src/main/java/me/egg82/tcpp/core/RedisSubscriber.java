package me.egg82.tcpp.core;

import org.bukkit.Bukkit;

import ninja.egg82.bukkit.utils.TaskUtil;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber extends JedisPubSub {
    //vars

    //constructor
    public RedisSubscriber() {
        super();
    }

    //public
    public void onMessage(String channel, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if (!channel.equals("tcpp")) {
            return;
        }

        String threadCommand = "troll " + message;

        TaskUtil.runSync(new Runnable() {
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), threadCommand);
            }
        });
    }

    //private

}
