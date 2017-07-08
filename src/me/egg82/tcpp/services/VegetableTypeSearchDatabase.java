package me.egg82.tcpp.services;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;

import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.sql.LanguageDatabase;

public class VegetableTypeSearchDatabase extends LanguageDatabase {
	//vars
	private IRegistry vegetableNameRegistry = (IRegistry) ServiceLocator.getService(VegetableNameRegistry.class);
	
	//constructor
	public VegetableTypeSearchDatabase() {
		Material[] types = new Material[] {
			Material.getMaterial("BEETROOT"),
			Material.BROWN_MUSHROOM,
			Material.CARROT_ITEM,
			Material.POTATO_ITEM,
			Material.RED_MUSHROOM
		};
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			String name = types[i].name();
			fields.add(name);
			fields.add((String) vegetableNameRegistry.getRegister(name));
			fields.addAll(Arrays.asList(name.split("_")));
			
			addRow(fields.toArray(new String[0]));
		}
	}
	
	//public
	
	//private
	
}
