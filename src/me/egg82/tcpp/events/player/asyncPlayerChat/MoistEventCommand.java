package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.MoistRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class MoistEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private IRegistry<UUID> moistRegistry = ServiceLocator.getService(MoistRegistry.class);
	
	private POSTagger tagger = ServiceLocator.getService(POSTagger.class);
	private WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
	
	//constructor
	public MoistEventCommand(AsyncPlayerChatEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (tagger == null) {
			tagger = ServiceLocator.getService(POSTagger.class);
			if (tagger == null) {
				return;
			}
		}
		
		Player player = event.getPlayer();
		
		if (!moistRegistry.hasRegister(player.getUniqueId())) {
			return;
		}
		
		String message = String.join(" ", lengthen(event.getMessage().split("\\s")));
		int startPosition = 0;
		int endPosition = 0;
		String rebuiltMessage = "";
		String[] tokens = tokenizer.tokenize(message.replaceAll("[^a-zA-Z0-9\\s']", " "));
		String[] tags = tagger.tag(tokens);
		
		for (int i = 0; i < tags.length; i++) {
			if (tokens[i].equals(" ")) {
				continue;
			}
			
			endPosition = message.indexOf(' ', startPosition + 1);
			if (endPosition == -1) {
				endPosition = message.length();
			}
			
			String oldToken = tokens[i];
			
			if (tags[i].equals("JJ")) {
				tokens[i] = "moist";
			} else if (tags[i].equals("NNP")) {
				tokens[i] = "Moist";
			} else if (tags[i].equals("NN")) {
				tokens[i] = (i == 0 || !tokens[i - 1].equals("moistness")) ? "moistness" : "";
			}
			if (CommandUtil.getPlayerByName(tags[i]) != null) {
				tokens[i] = "moist";
			}
			
			if (tokens[i] != "") {
				String trueToken = message.substring(startPosition, endPosition);
				int index = trueToken.indexOf(oldToken);
				
				rebuiltMessage += (index == 0) ? tokens[i] + trueToken.substring(oldToken.length()) + " " : trueToken.substring(0, index) + tokens[i] + trueToken.substring(index + oldToken.length()) + " ";
			}
			
			startPosition = endPosition + 1;
		}
		
		event.setMessage(rebuiltMessage.trim());
	}
	
	private String[] lengthen(String[] input) {
		ArrayList<String> retVal = new ArrayList<String>();
		
		for (String s : input) {
			if (s.equals("u")) {
				s = "you";
			} else if (s.equals("U")) {
				s = "You";
			} else if (s.equals("ur")) {
				s = "your";
			} else if (s.equalsIgnoreCase("ur")) {
				s = "Your";
			} else if (s.equals("r")) {
				s = "are";
			} else if (s.equals("R")) {
				s = "Are";
			} else if (s.equals("sup")) {
				retVal.add("what's");
				s = "up";
			} else if (s.equalsIgnoreCase("sup")) {
				retVal.add("What's");
				s = "up";
			} else if (s.equals("k") || s.equals("ok")) {
				s = "okay";
			} else if (s.equals("K") || s.equalsIgnoreCase("ok")) {
				s = "Okay";
			} else if (s.equals("prolly")) {
				s = "probably";
			} else if (s.equalsIgnoreCase("prolly")) {
				s = "Probably";
			} else if (s.equals("c")) {
				s = "see";
			} else if (s.equals("C")) {
				s = "See";
			} else if (s.equals("rn")) {
				retVal.add("right");
				s = "now";
			} else if (s.equalsIgnoreCase("rn")) {
				retVal.add("Right");
				s = "now";
			} else if (s.equals("brb")) {
				retVal.add("be");
				retVal.add("right");
				s = "back";
			} else if (s.equalsIgnoreCase("brb")) {
				retVal.add("Be");
				retVal.add("right");
				s = "back";
			} else if (s.equals("brt")) {
				retVal.add("be");
				retVal.add("right");
				s = "there";
			} else if (s.equalsIgnoreCase("brt")) {
				retVal.add("Be");
				retVal.add("right");
				s = "there";
			} else if (s.equals("hax")) {
				s = "hacks";
			} else if (s.equalsIgnoreCase("hax")) {
				s = "Hacks";
			} else if (s.equalsIgnoreCase("idc")) {
				retVal.add("I");
				retVal.add("don't");
				s = "care";
			} else if (s.equalsIgnoreCase("idk")) {
				retVal.add("I");
				retVal.add("don't");
				s = "know";
			} else if (s.equals("irl")) {
				retVal.add("in");
				retVal.add("real");
				s = "life";
			} else if (s.equalsIgnoreCase("irl")) {
				retVal.add("In");
				retVal.add("real");
				s = "life";
			} else if (s.equals("j/k") || s.equals("jk")) {
				retVal.add("just");
				s = "kidding";
			} else if (s.equalsIgnoreCase("j/k") || s.equalsIgnoreCase("jk")) {
				retVal.add("Just");
				s = "kidding";
			} else if (s.equals("nm") || s.equals("nvm") || s.equals("nevermind")) {
				retVal.add("never");
				s = "mind";
			} else if (s.equalsIgnoreCase("nm") || s.equalsIgnoreCase("nvm") || s.equalsIgnoreCase("nevermind")) {
				retVal.add("Never");
				s = "mind";
			} else if (s.equals("np")) {
				retVal.add("no");
				s = "problem";
			} else if (s.equalsIgnoreCase("np")) {
				retVal.add("No");
				s = "problem";
			} else if (s.equals("nsfw")) {
				retVal.add("not");
				retVal.add("safe");
				retVal.add("for");
				s = "work";
			} else if (s.equalsIgnoreCase("nsfw")) {
				retVal.add("Not");
				retVal.add("safe");
				retVal.add("for");
				s = "work";
			} else if (s.equals("oic")) {
				retVal.add("oh");
				retVal.add("I");
				s = "see";
			} else if (s.equalsIgnoreCase("oic")) {
				retVal.add("Oh");
				retVal.add("I");
				s = "see";
			} else if (s.equals("omw")) {
				retVal.add("on");
				retVal.add("my");
				s = "way";
			} else if (s.equalsIgnoreCase("omw")) {
				retVal.add("On");
				retVal.add("my");
				s = "way";
			} else if (s.equals("qt")) {
				s = "cutie";
			} else if (s.equalsIgnoreCase("qt")) {
				s = "Cutie";
			} else if (s.equals("ru")) {
				retVal.add("are");
				s = "you";
			} else if (s.equalsIgnoreCase("ru")) {
				retVal.add("Are");
				s = "you";
			} else if (s.equals("tmi")) {
				retVal.add("too");
				retVal.add("much");
				s = "information";
			} else if (s.equalsIgnoreCase("tmi")) {
				retVal.add("Too");
				retVal.add("much");
				s = "information";
			} else if (s.equals("wb")) {
				retVal.add("welcome");
				s = "back";
			} else if (s.equalsIgnoreCase("wb")) {
				retVal.add("Welcome");
				s = "back";
			}
			
			retVal.add(s);
		}
		
		return retVal.toArray(new String[0]);
	}
}
