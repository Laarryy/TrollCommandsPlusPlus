package me.egg82.tcpp.services.databases;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

import ninja.egg82.sql.LanguageDatabase;
import ninja.egg82.utils.ReflectUtil;

public class MonsterTypeSearchDatabase extends LanguageDatabase {
	//vars
	
	//constructor
	public MonsterTypeSearchDatabase() {
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
			if (!ReflectUtil.doesExtend(Monster.class, types[i].getEntityClass())) {
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
