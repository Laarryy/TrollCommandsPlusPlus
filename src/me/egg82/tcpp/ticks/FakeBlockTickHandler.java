package me.egg82.tcpp.ticks;

import me.egg82.tcpp.lists.FakeBlockSet;
import me.egg82.tcpp.reflection.block.IFakeBlockHelper;
import ninja.egg82.bukkit.core.BlockData;
import ninja.egg82.bukkit.handlers.async.AsyncTickHandler;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;

public class FakeBlockTickHandler extends AsyncTickHandler {
	//vars
	private IConcurrentSet<BlockData> fakeBlockSet = ServiceLocator.getService(FakeBlockSet.class);
	
	private IFakeBlockHelper fakeBlockHelper = ServiceLocator.getService(IFakeBlockHelper.class);
	
	//constructor
	public FakeBlockTickHandler() {
		super(0L, 1L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		fakeBlockHelper.sendAllMulti(fakeBlockSet);
	}
}