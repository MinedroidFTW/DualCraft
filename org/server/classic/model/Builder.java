package dualcraft.org.server.classic.model;

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

import java.util.Random;
import dualcraft.org.server.classic.model.Level;
import org.slf4j.*;

/**
 * Level Builder Interface.
 * 
 */

public abstract class Builder {
	protected byte[][][] m_blocks;

	protected static final Logger m_logger = LoggerFactory.getLogger(Builder.class);

	protected int m_height;
	
	protected int m_depth;
	
	protected int m_width;

	protected Random m_random;

	protected long m_seed;

	// for use in theming
	protected String m_theme;
	protected byte m_grassBlock;
	protected byte m_leavesBlock;

	public void setSeed(long seed) {
		m_seed = seed;
		m_random = new Random(seed);
	}

	public Builder(Level level) {
		setSummer();
		setLevel(level);
		m_seed = 0;
		m_random = new Random();
	}

	public void setLevel(Level level) {
		m_height = level.getHeight();
		m_width = level.getWidth();
		m_depth = level.getDepth();
		m_blocks = new byte[m_width][m_height][m_depth];
	}

	public abstract void generate();

	public byte[][][] getBlocks() {
		return (byte[][][])(m_blocks.clone());
	}

	public void setSummer() {
		m_grassBlock = BlockConstants.GRASS;
		m_leavesBlock = BlockConstants.LEAVES;
		m_theme = "Summer";
	}

	public void setWinter() {
		m_grassBlock = BlockConstants.CLOTH_WHITE;
		m_leavesBlock = BlockConstants.CLOTH_WHITE;
		m_theme = "Winter";
	}

	public void setOasis() {
		m_grassBlock = BlockConstants.SAND;
		m_leavesBlock = BlockConstants.LEAVES;
		m_theme = "Oasis";
	}
}
