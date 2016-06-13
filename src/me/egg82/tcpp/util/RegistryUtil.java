package me.egg82.tcpp.util;

import org.bukkit.entity.Player;

import me.egg82.tcpp.commands.AloneCommand;
import me.egg82.tcpp.commands.AnnoyCommand;
import me.egg82.tcpp.commands.BombCommand;
import me.egg82.tcpp.commands.BurnCommand;
import me.egg82.tcpp.commands.ControlCommand;
import me.egg82.tcpp.commands.DelayKillCommand;
import me.egg82.tcpp.commands.DisplayCommand;
import me.egg82.tcpp.commands.ElectrifyCommand;
import me.egg82.tcpp.commands.ExplodeBreakCommand;
import me.egg82.tcpp.commands.FreezeCommand;
import me.egg82.tcpp.commands.GarbleCommand;
import me.egg82.tcpp.commands.HauntCommand;
import me.egg82.tcpp.commands.HurtCommand;
import me.egg82.tcpp.commands.InfinityCommand;
import me.egg82.tcpp.commands.LagCommand;
import me.egg82.tcpp.commands.LavaBreakCommand;
import me.egg82.tcpp.commands.NauseaCommand;
import me.egg82.tcpp.commands.PopupCommand;
import me.egg82.tcpp.commands.RewindCommand;
import me.egg82.tcpp.commands.SlowMineCommand;
import me.egg82.tcpp.commands.SlowpokeCommand;
import me.egg82.tcpp.commands.SpamCommand;
import me.egg82.tcpp.commands.SpartaCommand;
import me.egg82.tcpp.commands.SpinCommand;
import me.egg82.tcpp.commands.StarveCommand;
import me.egg82.tcpp.commands.VegetableCommand;
import me.egg82.tcpp.commands.WeaklingCommand;

public class RegistryUtil {
	//vars
	
	//constructor
	public RegistryUtil() {
		
	}
	
	//public
	public static void intialize() {
		
	}
	
	public static void onQuit(String name, Player player) {
		//kills player
		new DelayKillCommand(null, null, null, null).onQuit(name, player);
		
		//multi-reg
		new ControlCommand(null, null, null, null).onQuit(name, player);
		new VegetableCommand(null, null, null, null).onQuit(name, player);
		new DisplayCommand(null, null, null, null).onQuit(name, player);
	}
	
	public static void onDeath(String name, Player player) {
		//kills player
		new BombCommand(null, null, null, null).onDeath(name, player);
		new ElectrifyCommand(null, null, null, null).onDeath(name, player);
		new BurnCommand(null, null, null, null).onDeath(name, player);
		new StarveCommand(null, null, null, null).onDeath(name, player);
		new HurtCommand(null, null, null, null).onDeath(name, player);
		new DelayKillCommand(null, null, null, null).onDeath(name, player);
		new InfinityCommand(null, null, null, null).onDeath(name, player);
		new SpartaCommand(null, null, null, null).onDeath(name, player);
		
		//multi-reg
		new VegetableCommand(null, null, null, null).onDeath(name, player);
		new DisplayCommand(null, null, null, null).onDeath(name, player);
	}
	
	
	public static void onLogin(String name, Player player) {
		//kills player
		new BombCommand(null, null, null, null).onLogin(name, player);
		new ElectrifyCommand(null, null, null, null).onLogin(name, player);
		new BurnCommand(null, null, null, null).onLogin(name, player);
		new StarveCommand(null, null, null, null).onLogin(name, player);
		new HurtCommand(null, null, null, null).onLogin(name, player);
		new SpartaCommand(null, null, null, null).onLogin(name, player);
		
		//tick
		new HauntCommand(null, null, null, null).onLogin(name, player);
		new SlowpokeCommand(null, null, null, null).onLogin(name, player);
		new SpinCommand(null, null, null, null).onLogin(name, player);
		new WeaklingCommand(null, null, null, null).onLogin(name, player);
		new SpamCommand(null, null, null, null).onLogin(name, player);
		new SlowMineCommand(null, null, null, null).onLogin(name, player);
		new NauseaCommand(null, null, null, null).onLogin(name, player);
		new AloneCommand(null, null, null, null).onLogin(name, player);
		new AnnoyCommand(null, null, null, null).onLogin(name, player);
		new RewindCommand(null, null, null, null).onLogin(name, player);
		new PopupCommand(null, null, null, null).onLogin(name, player);
		
		//event
		new ExplodeBreakCommand(null, null, null, null).onLogin(name, player);
		new LagCommand(null, null, null, null).onLogin(name, player);
		new LavaBreakCommand(null, null, null, null).onLogin(name, player);
		new GarbleCommand(null, null, null, null).onLogin(name, player);
		new FreezeCommand(null, null, null, null).onLogin(name, player);
	}
	
	//private
	
}
