package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.services.registries.MoistRegistry;
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
	
	private Pattern p = Pattern.compile("[^a-zA-Z0-9']");
	
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
		
		Player player = event.getPlayer();
		
		if (!moistRegistry.hasRegister(player.getUniqueId())) {
			return;
		}
		
		if (tagger == null) {
			tagger = ServiceLocator.getService(POSTagger.class);
			if (tagger == null) {
				return;
			}
		}
		
		String message = String.join(" ", lengthen(event.getMessage().split("\\s")));
		int startPosition = 0;
		int endPosition = 0;
		String rebuiltMessage = "";
		String[] tokens = replaceSpecialChars(p, tokenizer.tokenize(message));
		String[] tags = tagger.tag(tokens);
		
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals(" ")) {
				rebuiltMessage += message.charAt(startPosition);
				startPosition++;
				continue;
			}
			
			endPosition = patternIndexOf(p, message, startPosition + 1);
			if (endPosition == -1) {
				endPosition = message.length();
			}
			
			String oldToken = tokens[i];
			
			if (tags[i].equals("JJ") || tags[i].equals("RB") || tags[i].equals("RBR") || tags[i].equals("RBS")) {
				tokens[i] = (i <= 1 || (!tokens[i - 1].equals("moist") && !tokens[i - 2].equals("moist"))) ? "moist" : "moister";
			} else if (tags[i].equals("VB")) {
				tokens[i] = "moist";
			} else if (tags[i].equals("VBD")) {
				tokens[i] = "moisted";
			} else if (tags[i].equals("NNP")) {
				tokens[i] = "Moist";
			} else if (tags[i].equals("NNPS")) {
				tokens[i] = "Moists";
			} else if (tags[i].equals("NN")) {
				tokens[i] = (i == 0 || !tokens[i - 1].equals("moistness")) ? "moistness" : "";
			} else if (tags[i].equals("NNS")) {
				tokens[i] = (i == 0 || !tokens[i - 1].equals("moistnesses")) ? "moistnesses" : "";
			}
			if (CommandUtil.getPlayerByName(tags[i]) != null) {
				tokens[i] = "Moist";
			}
			tokens[i] = tryMoistPun(tokens[i]);
			
			if (!tokens[i].isEmpty()) {
				String trueToken = message.substring(startPosition, endPosition);
				int index = trueToken.indexOf(oldToken);
				
				rebuiltMessage += (index == 0) ? tokens[i] + trueToken.substring(oldToken.length()) : trueToken.substring(0, index) + tokens[i] + trueToken.substring(index + oldToken.length());
			}
			
			startPosition = endPosition;
		}
		
		if (endPosition < message.length()) {
			rebuiltMessage += message.substring(startPosition, message.length());
		}
		
		event.setMessage(rebuiltMessage.trim());
	}
	
	private String[] replaceSpecialChars(Pattern p, String[] input) {
		ArrayList<String> retVal = new ArrayList<String>();
		
		for (int i = 0; i < input.length; i++) {
			String replaced = input[i].replaceAll(p.pattern(), " ");
			String buffer = "";
			for (int j = 0; j < replaced.length(); j++) {
				if (replaced.charAt(j) == ' ') {
					if (!buffer.isEmpty()) {
						retVal.add(buffer);
						buffer = "";
					}
					retVal.add(" ");
				} else {
					buffer += replaced.charAt(j);
				}
			}
			if (!buffer.isEmpty()) {
				retVal.add(buffer);
			}
		}
		
		return retVal.toArray(new String[0]);
	}
	
	private int patternIndexOf(Pattern p, String message, int index) {
		Matcher m = p.matcher(message.substring(index));
		if (m.find()) {
			return index + m.start();
		}
		return -1;
	}
	
	private String tryMoistPun(String input) {
		input = input.replace("mist", "moist")
				.replace("myst", "moist")
				.replace("max", "moist")
				.replace("mace", "moisk")
				.replace("masc", "mois")
				.replace("mash", "moist")
				.replace("mask", "moist")
				.replace("mass", "moist")
				.replace("mast", "moist")
				.replace("maze", "moizt")
				.replace("mess", "moist")
				.replace("miss", "moist")
				.replace("misc", "moist")
				.replace("moss", "moist")
				.replace("most", "moist")
				.replace("mous", "moist")
				.replace("muck", "moist")
				.replace("mugg", "moist")
				.replace("muse", "moist")
				.replace("mush", "moist")
				.replace("must", "moist")
				.replace("hoist", "moist");
		
		return input;
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
