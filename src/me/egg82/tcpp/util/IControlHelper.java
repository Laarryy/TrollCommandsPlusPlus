package me.egg82.tcpp.util;

import org.bukkit.entity.Player;

public interface IControlHelper {
	//functions
	void control(String controllerUuid, Player controller, String uuid, Player player);
	void uncontrol(String controllerUuid, Player controller);
}
