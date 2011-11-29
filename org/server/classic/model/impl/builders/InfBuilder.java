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
 * Builds a level based off equations and a seed.
 * 
 */

public class InfBuilder extends Builder {

	private int m_scale = 32;

	public InfBuilder(Level level) {
		super(level);
	}

	public void  generate() {
		for (int x = 0; x < m_width; x++) {
			for (int y = 0; y < m_height; y++) {
				int i = m_width / 2 - m_width + x;
				int j = m_height / 2 - m_height + y;
				int k = calcPoint(i, j, m_seed);
				if (k < 0) k = 0;
				if (k > m_depth - 1) k = m_depth - 1;
				m_blocks[x][y][k] = BlockConstants.GRASS;
				for (int z = k - 1; z >= 0; z--) {
					m_blocks[x][y][z] = BlockConstants.DIRT;
				}
			}
		}
		simulateOceanFlood();
	}

	private int calcPoint(double x, double y, long seed) {
		//int k = m_scale;
		x = x * Math.PI / 12;
		y = y * Math.PI / 12;

		double base = (arctan(-8 * x) + arctan(-8 * y)) + (arctan(x / 2) + arctan(y / 2)) + (arctan(-1 * x / 10) + arctan(-1 * y / 10)) + (arctan(x / 20) + arctan(y / 80));
		double a = (cos(x / 18) + Math.pow(arctan(x), 2) / 2 + cos(y / 18) * Math.pow(arctan(y), 2) + Math.pow(cos(x / 2), 2)  + Math.pow(cos(y / 2), 3)) * m_scale;

		return (int)(a + base - m_depth * 1.05);
	}

	public void simulateOceanFlood() {
		int oceanLevel = m_depth / 2 - 1;
		for (int x = 0; x < m_width; x++) {
			for (int y = 0; y < m_height; y++) {
				if (m_blocks[x][y][oceanLevel] == BlockConstants.AIR) {
					floodBlock(x, y, oceanLevel);
				}
			}
		}
	}

	private void floodBlock(int x, int y, int oceanLevel) {
		for (int z = oceanLevel; true; z--) {
			if (z < 0) { break; }
				if (m_blocks[x][y][z] == BlockConstants.AIR) {
					m_blocks[x][y][z] = BlockConstants.WATER;
				} else if (m_blocks[x][y][z + 1] == BlockConstants.WATER && (m_blocks[x][y][z] == BlockConstants.DIRT || m_blocks[x][y][z] == BlockConstants.GRASS)) {
					m_blocks[x][y][z] = BlockConstants.SAND;
					break;
				}
			}
	}

	protected double sin(double value) {
		return Math.sin(value / (m_scale / 4));
	}

	protected double cos(double value) {
		return Math.cos(value / (m_scale / 4));
	
	}

	protected double tanh(double value) {
		return Math.tanh(value / (m_scale / 4));
	}

	protected double arctan(double value) {
		return Math.atan(value / (m_scale / 4));
	}

	protected double csc(double value) {
		return 1 / Math.sin(value / (m_scale / 4));
	}

	protected double sec(double value) {
		return 1 / Math.cos(value / (m_scale / 4));
	}
}
