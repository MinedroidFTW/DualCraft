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
 * Represents the brush used to "paint" the level with.
 * 
 */
public abstract class Brush {
	
	/**
	 * Creates the brush with the default radius.
	 */
	public Brush() {
	}
	
	/**
	 * Creates the brush with the specified radius.
	 * @param radius The radius of this brush.
	 */
	public Brush(int radius) {
	}
	
	/**
	 * Handles the "painting". Implementation should be done with paintBlocks
	 * @param level The level
	 * @param x x position
	 * @param y y position
	 * @param z z position
	 * @param mode
	 * @param type Type of block
	 */
	public abstract void paint(Player player, Level level, int x, int y, int z, int mode, int type);
	
	/**
	 * Set the radius
	 * @param newRadius
	 * @return Whether the radius was set
	 */
	public abstract boolean setRadius(int newRadius);
	
	/**
	 * Sets the width
	 * @param newWidth
	 * @return Return the width that was set
	 */
	public abstract int setWidth(int newWidth);
	
	/**
	 * Sets the height
	 * @param newHeight
	 * @return Return the height that was set
	 */
	public abstract int setHeight(int newHeight);
	
	/**
	 * Sets the length
	 * @param newLength
	 * @return Return the length that was set
	 */
	public abstract int setLength(int newLength);
	
	/**
	 * Sets whether this brush will delete also
	 * @param enable
	 * @return The old value
	 */
	public abstract boolean useForDelete(boolean enable);
	
	/**
	 * Returns whether this brush is used to delete
	 * @return If used for delete
	 */
	public abstract boolean getUseForDelete();
	
	/**
	 * Gets the width
	 * @return The width
	 */
	public abstract int getWidth();
	
	/**
	 * Gets the length
	 * @return The length
	 */
	public abstract int getLength();
	
	/**
	 * Gets the height
	 * @return The height
	 */
	public abstract int getHeight();
	
	/**
	 * Gets the radius
	 * @return The radius
	 */
	public abstract int getRadius();
}
