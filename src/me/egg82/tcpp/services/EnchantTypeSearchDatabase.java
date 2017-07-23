package me.egg82.tcpp.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.enchantments.Enchantment;

import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.sql.LanguageDatabase;

public class EnchantTypeSearchDatabase extends LanguageDatabase {
	//vars
	private IRegistry<String> enchantNameRegistry = ServiceLocator.getService(EnchantNameRegistry.class);
	
	//constructor
	public EnchantTypeSearchDatabase() {
		super();
		
		Enchantment[] types = Enchantment.values();
		
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
			String name = types[i].getName();
			fields.add(name);
			fields.add(enchantNameRegistry.getRegister(name, String.class));
			fields.addAll(Arrays.asList(name.split("_")));
			
			addRow(fields.toArray(new String[0]));
		}
	}
	
	//public
	
	//private
	
}
