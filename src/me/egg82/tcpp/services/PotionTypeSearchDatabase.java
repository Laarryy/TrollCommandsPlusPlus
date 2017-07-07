package me.egg82.tcpp.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.potion.PotionEffectType;

import ninja.egg82.sql.LanguageDatabase;

public class PotionTypeSearchDatabase extends LanguageDatabase {
	//vars
	
	//constructor
	public PotionTypeSearchDatabase() {
		PotionEffectType[] types = PotionEffectType.values();
		
		Arrays.sort(types, (a, b) -> {
			if (a == null) {
				if (b == null) {
					return 0;
				}
				return -1;
			}
			if (b == null) {
				return 1;
			}
			
			return a.getName().compareTo(b.getName());
		});
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			String name = types[i].getName().toLowerCase();
			fields.add(name);
			fields.addAll(Arrays.asList(name.split("_")));
			
			if (name == "confusion") {
				fields.add("nausea");
			} else if (name == "fast_digging") {
				fields.add("haste");
			} else if (name == "damage_resistance") {
				fields.add("resistance");
			} else if (name == "harm") {
				fields.add("instant damage");
			} else if (name == "heal") {
				fields.add("instant health");
			} else if (name == "increase_damage") {
				fields.add("strength");
			} else if (name == "jump") {
				fields.add("jump boost");
			} else if (name == "slow") {
				fields.add("slowness");
			} else if (name == "slow_digging") {
				fields.add("mining fatigue");
			} else if (name == "unluck") {
				fields.add("bad luck");
			}
			
			addRow(fields.toArray(new String[0]));
		}
	}
	
	//public
	
	//private
	
}
