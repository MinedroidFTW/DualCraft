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
import dualcraft.org.server.classic.model.BlockManager;
import dualcraft.org.server.classic.model.Level;

/**
 * A block behaviour that handles lava.
 * 
 */

public class LavaBehaviour implements BlockBehaviour {
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.queueActiveBlockUpdate(x, y, z);
	}
	
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}
	
	public void handleScheduledBehaviour(Level level, int x, int y, int z, int type) {
		// represents the different directions lava can spread
		// x, y, z
		int[][] spreadRules = { { 0, 0, -1 }, { 1, 0, 0 }, { -1, 0, 0 }, { 0, 1, 0 }, { 0, -1, 0 } };
		// then, spread outward
		for (int i = 0; i <= spreadRules.length - 1; i++) {
			byte thisOutwardBlock = level.getBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2]);
			
			// check for lava
			if (thisOutwardBlock == BlockConstants.WATER || thisOutwardBlock == BlockConstants.STILL_WATER) {
				level.setBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2], BlockConstants.ROCK);
			} else if (!BlockManager.getBlockManager().getBlock(thisOutwardBlock).isSolid() && !BlockManager.getBlockManager().getBlock(thisOutwardBlock).isLiquid()) {
				level.setBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2], BlockConstants.LAVA);
			}
		}
	}
	
}
