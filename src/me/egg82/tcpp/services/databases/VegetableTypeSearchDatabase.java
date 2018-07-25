package me.egg82.tcpp.services.databases;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;

import me.egg82.tcpp.registries.VegetableNameRegistry;
import ninja.egg82.bukkit.reflection.material.IMaterialHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.sql.LanguageDatabase;

public class VegetableTypeSearchDatabase extends LanguageDatabase {
	//vars
	private IVariableRegistry<String> vegetableNameRegistry = ServiceLocator.getService(VegetableNameRegistry.class);
	
	//constructor
	public VegetableTypeSearchDatabase() {
		super();
		
		IMaterialHelper materialHelper = ServiceLocator.getService(IMaterialHelper.class);
		
		Material[] types = new Material[] {
			Material.getMaterial("BEETROOT"),
			Material.BROWN_MUSHROOM,
			materialHelper.getByName("CARROT_ITEM"),
			materialHelper.getByName("POTATO_ITEM"),
			Material.RED_MUSHROOM
		};
		
		for (Material m : types) {
			if (m == null) {
				continue;
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			String name = m.name();
			fields.add(name);
			fields.add(vegetableNameRegistry.getRegister(name, String.class));
			fields.addAll(Arrays.asList(name.split("_")));
			
			addRow(fields.toArray(new String[0]));
		}
	}
	
	//public
	
	//private
	
}
