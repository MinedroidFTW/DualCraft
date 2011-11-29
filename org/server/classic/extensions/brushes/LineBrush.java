package dualcraft.org.server.classic.extensions.brushes;

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

import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Player;

/**
 * A brush that makes a line away from player
 * 
 */

public class LineBrush extends BrushAdapter {
	
	private static int BLOCKSIZE = 32;
	
	public LineBrush() {
		
	}
	
	public LineBrush(int radius) {
		
		setRadius(radius);
	}
	
	@Override
	protected void paintBlocks(Player player, Level level, int x, int y, int z, boolean adding, int type) {
		int[] playerPosition = new int[] { player.getPosition().getX() / BLOCKSIZE, player.getPosition().getY() / BLOCKSIZE, player.getPosition().getZ() / BLOCKSIZE };
		
		int dx = x - playerPosition[0];
		int dy = y - playerPosition[1];
		int dz = z - playerPosition[2] + 1;
		int adx = Math.abs(dx);
		int ady = Math.abs(dy);
		int adz = Math.abs(dz);
		
		int offsetZ = 0;
		int offsetY = 0;
		int offsetX = 0;
		
		if (adx > Math.max(ady, adz) && adx >= 1)
			offsetX = clamp(dx, -1, 1);
		else if (ady > Math.max(adx, adz) && ady >= 1)
			offsetY = clamp(dy, -1, 1);
		else if (adz > Math.max(adx, ady) && adz >= 1)
			offsetZ = clamp(dz, -1, 1);
		else
			return;
		
		for (int nthBlock = 0; nthBlock <= radius; nthBlock++)
			if (positionIsBuildable(level, offsetX * nthBlock + x, offsetY * nthBlock + y, offsetZ * nthBlock + z) == adding && Math.abs(offsetX) + Math.abs(offsetY) + Math.abs(offsetZ) <= Math.abs(radius))
				level.setBlock(offsetX * nthBlock + x, offsetY * nthBlock + y, offsetZ * nthBlock + z, type);
	}
}
