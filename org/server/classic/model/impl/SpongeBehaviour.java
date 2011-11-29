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

import dualcraft.org.server.classic.Configuration;
import dualcraft.org.server.classic.model.BlockBehaviour;
import dualcraft.org.server.classic.model.BlockConstants;
import dualcraft.org.server.classic.model.Level;

/**
 * Handles sponge behaviour.
 * 
 */

public class SpongeBehaviour implements BlockBehaviour {
	
	private int spongeRadius = Configuration.getConfiguration().getSpongeRadius();
	
	public void handlePassive(Level level, int x, int y, int z, int type) {
		for (int spongeX = -1 * spongeRadius; spongeX <= spongeRadius; spongeX++) {
			for (int spongeY = -1 * spongeRadius; spongeY <= spongeRadius; spongeY++) {
				for (int spongeZ = -1 * spongeRadius; spongeZ <= spongeRadius; spongeZ++) {
					if (level.getBlock(x + spongeX, y + spongeY, z + spongeZ) == BlockConstants.WATER || level.getBlock(x + spongeX, y + spongeY, z + spongeZ) == BlockConstants.STILL_WATER)
						level.setBlock(x + spongeX, y + spongeY, z + spongeZ, BlockConstants.AIR);
				}
			}
		}
	}
	
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		for (int spongeX = -1 * (spongeRadius + 1); spongeX <= spongeRadius + 1; spongeX++) {
			for (int spongeY = -1 * (spongeRadius + 1); spongeY <= spongeRadius + 1; spongeY++) {
				for (int spongeZ = -1 * (spongeRadius + 1); spongeZ <= spongeRadius + 1; spongeZ++) {
					if (level.getBlock(x + spongeX, y + spongeY, z + spongeZ) == BlockConstants.WATER)
						level.queueActiveBlockUpdate(x + spongeX, y + spongeY, z + spongeZ);
				}
			}
		}
	}
	
	public void handleScheduledBehaviour(Level level, int x, int y, int z, int type) {
		
	}
	
}
