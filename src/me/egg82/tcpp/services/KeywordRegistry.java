package me.egg82.tcpp.services;

import ninja.egg82.patterns.Registry;

public class KeywordRegistry extends Registry {
	//vars
	
	//constructor
	public KeywordRegistry() {
		super();
		
		this.setRegister("alone", String[].class, new String[] {
			"hide",
			"sneak"
		});
		this.setRegister("amnesia", String[].class, new String[] {
			"forget",
			"remember",
			"duplicate",
			"copy",
			"remove"
		});
		this.setRegister("annoy", String[].class, new String[] {
			"villager",
			"person"
		});
	}
	
	//public
	
	//private
	
}
