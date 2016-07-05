package com.wipe.zc.journey.lib.drag;

import com.wipe.zc.journey.lib.drag.SwipeLayout.Status;

public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
