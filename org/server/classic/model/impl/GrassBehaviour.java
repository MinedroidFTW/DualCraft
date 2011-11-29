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
 * Handles the spreading of grass blocks to adjacent, sun-exposed dirt blocks.
 * 
 */
public class GrassBehaviour implements BlockBehaviour {

	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}
	
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.queueActiveBlockUpdate(x, y, z);
	}
	
	public void handleScheduledBehaviour(Level level, int x, int y, int z, int type) {
		// do we need to die?

		byte aboveBlock = level.getBlock(x, y, z + 1);
		// Should the block above us kill us?
		if (BlockManager.getBlockManager().getBlock(aboveBlock).doesBlockLight() || BlockManager.getBlockManager().getBlock(aboveBlock).isLiquid()) {
			level.setBlock(x, y, z, BlockConstants.DIRT);
			return;
		}

		//spread
		for (int h = (x == 0 ? 0 : -1); h <= (x == level.getWidth() - 1 ? 0 : 1); h++) {
			for (int i = (y == 0 ? 0 : -1); i <= (y == level.getHeight() - 1 ? 0 : 1); i++) {
				for (int j = (z == 0 ? 0 : -1); j <= (z == level.getDepth() - 1 ? 0 : 1); j++) {
					if (level.getBlock(x + h, y + i, z + j) == BlockConstants.DIRT) {
						if (z + j < level.getDepth()) {
							// Is the block above it liquid?
							if (BlockManager.getBlockManager().getBlock(level.getBlock(x + h, y + i, z + j + 1)).isLiquid()) {
								break;
							}

							// Is the block smothered by something?
							if (BlockManager.getBlockManager().getBlock(level.getBlock(x + h, y + i, z + j + 1)).doesBlockLight()) {
								break;
							}
						}
			
						// Grow the grass
						level.setBlock(x + h, y + i, z + j, BlockConstants.GRASS);
					}
				}
			}
		}
	}
}
