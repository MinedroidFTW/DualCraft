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
import dualcraft.org.server.classic.model.BlockManager;
import dualcraft.org.server.classic.model.Level;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A block behaviour that handles water. Takes into account water's preference
 * for downward flow.
 * 
 */
public class WaterBehaviour implements BlockBehaviour {
	
	public void handlePassive(Level level, int x, int y, int z, int type) {
		level.queueActiveBlockUpdate(x, y, z);
	}
	
	public void handleDestroy(Level level, int x, int y, int z, int type) {
		
	}
	
	public void handleScheduledBehaviour(Level level, int x, int y, int z, int type) {
		
		// represents different directions in the Cartesian plane, z axis is
		// ignored and handled specially
		Integer[][] spreadRules = { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 1, 0 }, { 0, -1, 0 } };
		ArrayList<Integer[]> shuffledRules = new ArrayList<Integer[]>();
		for(Integer[] rule : spreadRules)
			shuffledRules.add(rule);
		Collections.shuffle(shuffledRules);
		spreadRules = shuffledRules.toArray(spreadRules);
		
		int spongeRadius = Configuration.getConfiguration().getSpongeRadius();
		

		for (int spongeX = (-1 * spongeRadius); spongeX <= spongeRadius; spongeX++) {
			for (int spongeY = (-1 * spongeRadius); spongeY <= spongeRadius; spongeY++) {
				for (int spongeZ = (-1 * spongeRadius); spongeZ <= spongeRadius; spongeZ++) {
					if (level.getBlock(x + spongeX, y + spongeY, z + spongeZ) == BlockConstants.SPONGE)
						return;
				}
			}
		}

		
		byte underBlock = level.getBlock(x, y, z - 1);
		// there is lava under me
		if (underBlock == BlockConstants.LAVA || underBlock == BlockConstants.STILL_LAVA) {
			level.setBlock(x, y, z, BlockConstants.AIR);
			level.setBlock(x, y, z - 1, BlockConstants.ROCK);
		// move me down
		} else if (!BlockManager.getBlockManager().getBlock(underBlock).isSolid() && !BlockManager.getBlockManager().getBlock(underBlock).isLiquid()) {
			level.setBlock(x, y, z - 1, BlockConstants.WATER);
			level.setBlock(x, y, z, BlockConstants.AIR);
		// spread outward
		} else {
			OUTERMOST_OUTWARD: for (int i = 0; i <= spreadRules.length - 1; i++) {
				byte thisOutwardBlock = level.getBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2]);
				
				for (int spongeX = (-1 * spongeRadius); spongeX <= spongeRadius; spongeX++) {
					for (int spongeY = (-1 * spongeRadius); spongeY <= spongeRadius; spongeY++) {
						for (int spongeZ = (-1 * spongeRadius); spongeZ <= spongeRadius; spongeZ++) {
							if (level.getBlock(x + spreadRules[i][0] + spongeX, y + spreadRules[i][1] + spongeY, z + spreadRules[i][2] + spongeZ) == BlockConstants.SPONGE)
								break OUTERMOST_OUTWARD;
						}
					}
				}
				
				// check for lava
				if (thisOutwardBlock == BlockConstants.LAVA || thisOutwardBlock == BlockConstants.STILL_LAVA) {
					level.setBlock(x, y, z, BlockConstants.AIR);
					level.setBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2], BlockConstants.ROCK);
				} else if (level.getBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2] - 1) == BlockConstants.AIR &&
					   level.getBlock(x, y, z - 1) == BlockConstants.WATER) {
					break OUTERMOST_OUTWARD;
				} else if (!BlockManager.getBlockManager().getBlock(thisOutwardBlock).isSolid() && !BlockManager.getBlockManager().getBlock(thisOutwardBlock).isLiquid()) {
					//level.setBlock(x, y, z, BlockConstants.AIR);
					level.setBlock(x + spreadRules[i][0], y + spreadRules[i][1], z + spreadRules[i][2], BlockConstants.WATER);
				}
			}
		}
	}
}
