package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.registries.SpamRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class SpamTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> spamRegistry = ServiceLocator.getService(SpamRegistry.class);
	
	private String[] spam = new String[] {
		"<Totally Legit> MAKE MONEY FROM HOME ONLINE! NO GIMMICKS! Call now!",
		"<Not A Scam> Get a FREE $1,000 Minecraft gift card! Just type in your bank account information!",
		"<Mr Nope> NEW! JUST RELEASED! Get the Apple iPod 3 FREE! Join now!",
		"<McHappyface> Learn to avoid scams! ONLY $1,00/CLASS! It's a STEAL! Call us today!",
		"<Clicky Me> Reply \"OK\" to receive a BRAND NEW gift card! YOUR $1,000 GIFT CARD IS WAITING!",
		"<Richy Richardson> REAL HOME JOBS! Get paid UP TO $2,000 EVERY WEEK like me! Click now!",
		"<Late Night> You can make $1,000 A WEEK working from home! As seen on CNN, MSNBC, USA Today, and ABC! Don't put it off, call us now!",
		"<Obama> Get the grants that YOU voted for! Click here now!",
		"<McBurger & Fries> How to lose weight.. AND KEEP IT OFF! Must read! Click now!",
		"<Nigerian Prince> I have 1 MILLION USD in a bank for you. I just need $1,000 to release it to you. Please reply, it's of utmost urgency!",
		"<Couch Potato> STOP WORKING SO HARD! Make $1,000/DAY! Call now!",
		"<Not A Virus> WARNING! Your computer clock MAY BE WRONG! Click here to download our software and fix it now!",
		"<MySpace> Will you VOTE for Zuckerberg in '98? Click here to vote TODAY!",
		"<Sumo Wrestler> 2 SIMPLE RULES to a flat stomach! Only $1,000 to find out!",
		"<Totes McGoats Legit> Make CASH by TAKING SURVEYS! Up to $1,000 IN SURVEYS EVERY DAY! Click here to find out more!",
		"<Lazy Pharma Inc> LOSE WEIGHT and INCREASE ENERGY! Take our pills now! SAFE, fast, and reliable!",
		"<Apel> BRAND NEW Apple iPhone 2 2GB (buy 3 get 1 free) $3,00! Call now!",
		"<Toally A Bajillionaire> Get paid EVERY SECOND! Earn up to $1 BILLION DOLLARS A DAY! Click to learn more!",
		"<Next Door Neighbor> Lonely singles IN YOUR AREA want to meet you! Go outside once in a while!",
		"<One Weird Trick> Use this ONE WEIRD TRICK to cut down on belly fat! Click now!",
		"<Martha Stewart> Politicians HATE HER! Learn how she beat the system and ruled the world in one night!",
		"<WannaCry> Your HD content pack is missing! Download one now! Click here!",
		"<Not A Surgeon> Wrinkle solution HORRIFIES SURGEONS! Find out now! Click here!",
		"<Bank of Egypt> Your payment has been sent! REPLY NOW!",
		"<Some Banker> CONTACT ME FOR YOUR TRANSFER OF $100,000 USD! Reply ASAP!",
		"<Western Union> VITAL INFORMATION regarding your bank transfer! Reply!",
		"<419> Dear beneficiary, we are resending this notification to your regarding your online money transfer. Please reply!",
		"<Ugly Matilda> Online dating, local singles picked just for YOU! Click now!",
		"<Legit Job> Do you speak ENGLISH? Get paid now! Reply below!",
		"<Indian Tech> Your computer has many WIRUS! Clean now! Call today!",
		"<Artisan Cook> Weird cooking ingredient to CURE ANY DISEASE! Only $100! Call now!",
		"<Bank You Never Heard Of> FREE credit card worth $1 MILLION! Apply today!",
		"<Some Person> PROPOSAL WORTH $1,000,000! REPLY NOW!"
	};
	
	//constructor
	public SpamTickCommand() {
		super(0L, 15L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : spamRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() < 0.35d) {
			player.sendMessage(spam[MathUtil.fairRoundedRandom(0, spam.length - 1)]);
		}
	}
}
