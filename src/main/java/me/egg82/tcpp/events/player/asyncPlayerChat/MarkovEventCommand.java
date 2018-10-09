package me.egg82.tcpp.events.player.asyncPlayerChat;

import me.egg82.tcpp.lists.MarkovSet;
import me.egg82.tcpp.registries.MarkovRegistry;
import me.egg82.tcpp.registries.MarkovSpeechRegistry;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.markov.MarkovChain;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class MarkovEventCommand extends EventHandler<AsyncPlayerChatEvent> {
    //vars
    private IConcurrentSet<UUID> markovSet = ServiceLocator.getService(MarkovSet.class);
    private IRegistry<UUID, UUID> markovRegistry = ServiceLocator.getService(MarkovRegistry.class);
    private IRegistry<UUID, String> markovSpeechRegistry = ServiceLocator.getService(MarkovSpeechRegistry.class);

    private String[] noise = new String[] {
            "I am a pretty pretty princess.",
            "I am the prettiest princess in all the land.",
            "I love Minecraft so much!",
            "This server is the best!",
            "I just love being on this server so much.",
            "The staff is fantastic!",
            "The blocks are wonderful!",
            "So colorful!",
            "Coooloooors..",
            "Hands! Many hands.",
            "If you were a unicorn, what type would you be?",
            "Taste the rainbow!",
            "This game is even better than Terraria!",
            "I love you all!",
            "This is my favorite place to be.",
            "I love tacos!",
            "Burritos are alright, but tacos are where it's at.",
            "You got them soft-shells, and them hard-shells..",
            "Just imagine that crunchy lettuce.",
            "It's amazing.",
            "Try a taco today!",
            "Not sponsored by Paper MC."
    };

    //constructor
    public MarkovEventCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        if (event.isCancelled()) {
            return;
        }

        // Single player
        if (markovSet.contains(event.getPlayer().getUniqueId())) {
            MarkovChain<String> chain = new MarkovChain<>(1);
            chain.add(event.getMessage().split("\\s+"), 6);
            for (UUID key : markovSpeechRegistry.getKeys()) {
                chain.add(markovSpeechRegistry.getRegister(key).split("\\s+"), 5);
            }
            for (String n : noise) {
                chain.add(n.split("\\s+"), 1);
            }

            markovSpeechRegistry.setRegister(event.getPlayer().getUniqueId(), event.getMessage()); // We set it twice to ensure a nice bit of randomness to messages even with only one player

            event.setMessage(String.join(" ", chain.chain()));
            return;
        }

        markovSpeechRegistry.setRegister(event.getPlayer().getUniqueId(), event.getMessage()); // Set it here so even if there's only one player online it'll mix  with their previous messages

        // Two players
        UUID other = markovRegistry.getRegister(event.getPlayer().getUniqueId());
        if (other == null) {
            other = markovRegistry.getKey(event.getPlayer().getUniqueId());
        }
        if (other == null) {
            return;
        }

        String otherSpeech = markovSpeechRegistry.getRegister(other);
        if (otherSpeech == null) {
            return;
        }

        MarkovChain<String> chain = new MarkovChain<>(1);
        chain.add(event.getMessage().split("\\s+"), 12);
        chain.add(otherSpeech.split("\\s+"), 10);
        for (UUID key : markovSpeechRegistry.getKeys()) {
            chain.add(markovSpeechRegistry.getRegister(key).split("\\s+"), 1);
        }
        for (String n : noise) {
            chain.add(n.split("\\s+"), 1);
        }

        event.setMessage(String.join(" ", chain.chain()));
    }
}
