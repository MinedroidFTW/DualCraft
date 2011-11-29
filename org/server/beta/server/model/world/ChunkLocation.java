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
package dualcraft.org.server.beta.server.model.world;

/**
 * A class that defines a chunk location
 * Used to identify loaded chunks in the requested world
 * 
 * 
 * @version 1.0.0.0
 */
public class ChunkLocation {

    /**
     * Creates a new chunk location
     * 
     * @param x The X coordinate to use
     * @param z The Z coordinate to use
     * @since 1.0.0.0
     */
    public ChunkLocation(final int x, final int z) {
        this.x = x;
        this.z = z;
    }
    
    /**
     * The X coordinate
     * 
     * @since 1.0.0.0
     */
    private transient final int x;
        
    /**
     * The Z coordinate
     * 
     * @since 1.0.0.0
     */
    private transient final int z;

    /**
     * Gets the X coordinate
     * 
     * @return The X coordinate
     * @since 1.0.0.0
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the Z coordinate
     * 
     * @return The Z coordinate
     * @since 1.0.0.0
     */
    public int getZ() {
        return this.z;
    }

    /**
     * Gets the hash code of this location
     * 
     * @return The hash code
     * @since 1.0.0.0
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + z;
        return result;
    }

    /**
     * Checks to see if another object is equal to this location
     * 
     * @param otherObject The other object that should be a ChunkLocation
     * @return True if the other object is the same
     * @since 1.0.0.0
     */
    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        ChunkLocation otherIdentifier = (ChunkLocation) otherObject;
        if (x != otherIdentifier.x) {
            return false;
        }
        if (z != otherIdentifier.z) {
            return false;
        }
        return true;
    }
}
