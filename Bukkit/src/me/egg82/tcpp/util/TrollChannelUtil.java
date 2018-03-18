package me.egg82.tcpp.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import ninja.egg82.exceptions.ArgumentNullException;
import ninja.egg82.plugin.utils.ChannelUtil;

public class TrollChannelUtil {
	//vars
	
	//constructor
	public TrollChannelUtil() {
		
	}
	
	//public
	public static void broadcastTroll(String args) {
		if (args == null) {
			throw new ArgumentNullException("args");
		}
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		
		if (!ChannelUtil.writeAll(out, new Object[] { "troll " + args })) {
			return;
		}
		
		ChannelUtil.broadcastToBukkit("Troll", stream.toByteArray());
	}
	
	//private
	
}
