package me.egg82.tcpp.databases;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.ReflectUtil;

public class MobTypeSearchDatabase extends LanguageDatabase {
	//vars
	
	//constructor
	public MobTypeSearchDatabase() {
		super();
		
		EntityType[] types = EntityType.values();
		
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
			
			return a.name().compareTo(b.name());
		});
		
		for (int i = 0; i < types.length; i++) {
			if (types[i] == null) {
				continue;
			}
			if (!ReflectUtil.doesExtend(Creature.class, types[i].getEntityClass())) {
				continue;
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			String name = types[i].name();
			fields.add(name);
			fields.add(WordUtils.capitalize(String.join(" ", name.toLowerCase().split("_"))));
			fields.addAll(Arrays.asList(name.split("_")));
			
			addRow(fields.toArray(new String[0]));
		}
	}
	
	//public
	
	//private
	
}
