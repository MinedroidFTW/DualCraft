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

import java.util.Random;

/**
 * A block behaviour that handles plants which require dirt and sunlight to
 * survive.
 * 
 */
public class SurfacePlantBehaviour implements BlockBehaviour {
	
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}
	
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.queueActiveBlockUpdate(x, y, z);
	}
	
	public void handleScheduledBehaviour(Level level, int x, int y, int z, int type) {
		level.queueActiveBlockUpdate(x, y, z);
		Random r = new Random();
		if (BlockManager.getBlockManager().getBlock(level.getBlock(x, y, z - 1)).getId() != BlockConstants.DIRT && BlockManager.getBlockManager().getBlock(level.getBlock(x, y, z - 1)).getId() != BlockConstants.GRASS || level.getLightDepth(x, y) > z || BlockManager.getBlockManager().getBlock(level.getBlock(x, y, z + 1)).isLiquid()) {
			level.setBlock(x, y, z, BlockConstants.AIR);
		}
		int spongeRadius = 2;
		for (int spongeX = -1 * spongeRadius; spongeX <= spongeRadius; spongeX++) {
			for (int spongeY = -1 * spongeRadius; spongeY <= spongeRadius; spongeY++) {
				for (int spongeZ = -1 * spongeRadius; spongeZ <= spongeRadius; spongeZ++) {
					if (level.getBlock(x + spongeX, y + spongeY, z + spongeZ) == BlockConstants.WATER || level.getBlock(x + spongeX, y + spongeY, z + spongeZ) == BlockConstants.STILL_WATER && r.nextBoolean() ) {
						level.setBlock(x + spongeX, y + spongeY, z + spongeZ, BlockConstants.AIR);
						return;
					}
				}
			}
		}
	}
	
}
