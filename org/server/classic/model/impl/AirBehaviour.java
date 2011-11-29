package dualcraft.org.server.classic.model.impl;

/*License
====================
Copyright (c) 2010-2012 Daniel Vidmar

We use a modified GNU gpl v 3 license for this.

GNU gpl v 3 is included in License.txt

The modified part of the license is some additions which state the following:

"Redistributions of this project in source or binary must give credit to UnXoft Interactive and DualCraft"
"Redistributions of this project in source or binary must modify at least 300 lines of code in order to release
an initial version. This will require documentation or proof of the 300 modified lines of code."
"Our developers reserve the right to add any additions made to a redistribution of DualCraft into the main
project"
"Our developers reserver the right if they suspect a closed source software using any code from our project
to request to overview the source code of the suspected software. If the owner of the suspected software refuses 
to allow a devloper to overview the code then we shall/are granted the right to persue legal action against
him/her"*/

import dualcraft.org.server.classic.model.BlockBehaviour;
import dualcraft.org.server.classic.model.BlockConstants;
import dualcraft.org.server.classic.model.Level;

/**
 * A block behaviour that handles water. Takes into account water's preference
 * for downward flow.
 * 
 */
public class AirBehaviour implements BlockBehaviour {
	
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.queueActiveBlockUpdate(x, y, z);
	}
	
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}
	
	public void handleScheduledBehaviour(Level level, int x, int y, int z, int type) {
		if (level.getBlock(x+1, y, z) == BlockConstants.WATER &&  level.getBlock(x-1, y, z) == BlockConstants.WATER && level.getBlock(x, y+1, z) == BlockConstants.WATER && level.getBlock(x, y-1, z) == BlockConstants.WATER) {
			level.setBlock(x, y, z, BlockConstants.WATER);
		}
		if (z < level.getDepth()/2 && z > level.getDepth()/2-3) {
			if (x == 0)
				level.setBlock(x, y, z, BlockConstants.WATER);
			if (x == level.getWidth()-1)
				level.setBlock(x, y, z, BlockConstants.WATER);
			if (y == 0)
				level.setBlock(x, y, z, BlockConstants.WATER);
			if (y == level.getHeight()-1)
				level.setBlock(x, y, z, BlockConstants.WATER);
		}
		if (level.getBlock(x, y, z+1) == BlockConstants.WATER) {
			level.setBlock(x, y, z, BlockConstants.WATER);
			level.setBlock(x, y, z+1, BlockConstants.AIR);
		}
	}
}
