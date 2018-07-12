package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.egg82.tcpp.registries.WhoAmIRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.patterns.tuples.pair.Pair;
import ninja.egg82.utils.MathUtil;

public class WhoAmITickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> whoAmIRegistry = ServiceLocator.getService(WhoAmIRegistry.class);
	
	private int numPlayers = 0;
	private List<Pair<String, String>> players = null;
	private String[] startingList = new String[] {
		"AntexThePanda",
		"JessiMorrison",
		"HiNinjaPvP",
		"LK911",
		"PuritianSwag",
		"2BB",
		"Iost",
		"Tofy_",
		"eduin",
		"Phosed",
		"RaspBerryDust",
		"Posesivo",
		"ItsLunor",
		"ComputerBoy122",
		"BusinessFries",
		"Random_Zealot",
		"Ghett0",
		"Enlargement",
		"awsucker",
		"tiimo",
		"9gd",
		"glorea",
		"shootinqstar",
		"Beings",
		"SlayZee_",
		"leandrozocker",
		"fairytalee",
		"DarkSlayerPL",
		"BlurPlayz",
		"Haxlegit",
		"NoteLeBoss",
		"SplitPushing",
		"1676",
		"Raikkou",
		"ItsCheats",
		"BinSystem",
		"kochyvess",
		"DogTreats",
		"Srta_Hill",
		"lacy3637",
		"kondziakk_",
		"Currysirloinrice",
		"5TEAK",
		"TBNRhack",
		"Master_Otto",
		"0xBEEF",
		"Xx_GrimReaper_xX",
		"DontChooseMe",
		"StateFarmIsHere",
		"Nodebot",
		"perns",
		"LouisL",
		"Tylarzz",
		"iTz_KillerIQ",
		"zFly_",
		"MU_Panda",
		"Nachos500",
		"Nesi",
		"FidgurAbdul",
		"Chippy_Senpai",
		"Spottay",
		"HilfeMama",
		"rAped",
		"Kevxo",
		"ognisty596",
		"tideh",
		"TijgerDavy",
		"Tannatron",
		"Game_",
		"lol_indra",
		"GodToquiii",
		"Muddykip",
		"LukaBabic",
		"xPapiDani",
		"Panda_76",
		"Focoh",
		"El_Jorge",
		"Lasciviously",
		"rood997",
		"The_Shrouded_One",
		"vectorRojas",
		"MLGilluminati",
		"Fantaz_",
		"Xelia_",
		"froutt",
		"Twixsy"
	};
	
	//constructor
	public WhoAmITickCommand() {
		super(0L, 15L);
		
		players = merge(startingList, Bukkit.getOfflinePlayers());
		numPlayers = Bukkit.getOfflinePlayers().length;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : whoAmIRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.2) {
			if (Bukkit.getOfflinePlayers().length > numPlayers) {
				players = merge(startingList, Bukkit.getOfflinePlayers());
				numPlayers = Bukkit.getOfflinePlayers().length;
			}
			
			player.setDisplayName(players.get(MathUtil.fairRoundedRandom(0, players.size() - 1)).getLeft());
			player.setPlayerListName(players.get(MathUtil.fairRoundedRandom(0, players.size() - 1)).getRight());
		}
	}
	
	private List<Pair<String, String>> merge(String[] one, OfflinePlayer[] two) {
		HashSet<Pair<String, String>> retVal = new HashSet<Pair<String, String>>();
		
		for (int i = 0; i < one.length; i++) {
			retVal.add(new Pair<String, String>(one[i], one[i]));
		}
		for (int i = 0; i < two.length; i++) {
			if (two[i].isOnline()) {
				Player p = CommandUtil.getPlayerByUuid(two[i].getUniqueId());
				retVal.add(new Pair<String, String>(p.getDisplayName(), p.getPlayerListName()));
			} else {
				retVal.add(new Pair<String, String>(two[i].getName(), two[i].getName()));
			}
		}
		
		return new ArrayList<Pair<String, String>>(retVal);
	}
}
