package me.egg82.tcpp.api.trolls;

import co.aikar.commands.CommandIssuer;
import me.egg82.tcpp.api.BukkitTroll;
import me.egg82.tcpp.api.TrollType;
import me.egg82.tcpp.enums.Message;
import ninja.egg82.events.BukkitEventFilters;
import ninja.egg82.events.BukkitEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class GarbleTroll extends BukkitTroll {
    private final Plugin plugin;

    public GarbleTroll(Plugin plugin, UUID playerID, TrollType type) {
        super(playerID, type);

        this.plugin = plugin;
    }

    public void start(CommandIssuer issuer) throws Exception {
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            api.stopTroll(this, issuer);
            return;
        }

        events.add(
                BukkitEvents.subscribe(plugin, AsyncPlayerChatEvent.class, EventPriority.LOW)
                        .filter(BukkitEventFilters.ignoreCancelled())
                        .handler(this::garbleChat)
        );

        issuer.sendInfo(Message.GARBLE__START, "{player}", player.getName());
    }

    public void stop(CommandIssuer issuer) throws Exception {
        super.stop(issuer);
        Player player = Bukkit.getPlayer(playerID);
        if (player == null) {
            return;
        }

        issuer.sendInfo(Message.GARBLE__STOP, "{player}", player.getName());
    }

    private void garbleChat(AsyncPlayerChatEvent ev) {
        Player p = ev.getPlayer();

        if (playerID.equals(p.getUniqueId())) {
            List<String> words = Arrays.asList(ev.getMessage().split(" "));
            for (int i = 0; i < words.size(); i++) {
                words.set(i, shuffle(words.get(i)));
            }
            Collections.shuffle(words);
            ev.setMessage(String.join(" ", words));
        }
    }

    private String shuffle(String input){
        List<Character> characters = new ArrayList<>();
        StringBuilder output = new StringBuilder(input.length());

        for(char c : input.toCharArray()){
            characters.add(c);
        }
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }

        return output.toString();
    }
}
