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

/**
 * Represents a location in the game world.
 * 
 */
public final class Position {
	
	/**
	 * X position.
	 */
	private final int x;
	
	/**
	 * Y position.
	 */
	private final int y;
	
	/**
	 * Z position.
	 */
	private final int z;
	
	/**
	 * Creates a new position.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the x coordinate.
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the y coordinate.
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Gets the z coordinate.
	 * @return The z coordinate.
	 */
	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			Position pos = (Position)other;
			return pos.z == z && pos.y == y && pos.x == x;
		}
		return false;
	}
}
