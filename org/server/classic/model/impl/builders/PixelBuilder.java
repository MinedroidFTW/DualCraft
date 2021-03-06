package dualcraft.org.server.classic.model.impl.builders;

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

import dualcraft.org.server.classic.model.Builder;
import dualcraft.org.server.classic.model.BlockConstants;
import dualcraft.org.server.classic.model.Level;

/**
 * Builds a level.
 * 
 */

public class PixelBuilder extends Builder {

	public PixelBuilder(Level level) {
		super(level);
	}

	public void  generate() {
		for(int x = 0; x < m_width; x++) {
			for(int z = 0; z < m_depth; z++) {
				m_blocks[x][0][z] = BlockConstants.CLOTH_WHITE;
				m_blocks[x][m_height-1][z] = BlockConstants.CLOTH_WHITE;
			}
		}

		for(int y = 1;y < m_height - 1; y++) {
			for(int z = 0; z < m_depth; z++) {
				m_blocks[0][y][z] = BlockConstants.CLOTH_WHITE;
				m_blocks[m_width-1][y][z] = BlockConstants.CLOTH_WHITE;
			}
		}
	}
}
