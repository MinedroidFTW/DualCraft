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
 * A Brush that makes boxes
 * 
 */

public class BoxBrush extends BrushAdapter {
	
	public BoxBrush() {
	}
	
	public BoxBrush(int radius) {
		setRadius(radius);
	}
	
	@Override
	protected void paintBlocks(Player player, Level level, int x, int y, int z, boolean adding, int type) {
		
		setOffsetsFromPerspective(player);
		
		for (int offsetZ = zOffStart; offsetZ <= zOffEnd; offsetZ++)
			for (int offsetY = yOffStart; offsetY <= yOffEnd; offsetY++)
				for (int offsetX = xOffStart; offsetX <= xOffEnd; offsetX++)
					if (positionIsBuildable(level, offsetX + x, offsetY + y, offsetZ + z) == adding)
						level.setBlock(offsetX + x, offsetY + y, offsetZ + z, type);
	}
	
}
