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
 * A class that defines a single chunk
 * 
 * 
 * @version 0.1.0.0
 */
public class Chunk {
    
    /**
     * Creates a new chunk
     * 
     * @param location The location to use
     * @param world The world the chunk is in
     * @since 0.1.0.0
     */
    public Chunk(ChunkLocation location, World world) {
        this.location = location;
        this.world = world;
        //TODO Sanity check
    }
    
    /**
     * The chunk location
     * 
     * @since 0.1.0.0
     */
    private ChunkLocation location;
    
    /**
     * Gets the chunk's location
     * 
     * @return The location
     * @since 0.1.0.0
     */
    public ChunkLocation getLocation() {
        return this.location;
    }
    
    /**
     * Sets the chunk's location
     * 
     * @param location The location
     * @since 0.1.0.0
     * @deprecated Use the constructor instead!
     */
    public void setLocation(final ChunkLocation location) {
        this.location = location;
    }
    
    /**
     * The world
     * 
     * @since 0.1.0.0
     */
    private World world;
    
    /**
     * Gets the chunk's world
     * 
     * @return The world
     * @since 0.1.0.0
     */
    public World getWorld() {
        return this.world;
    }
    
    /**
     * Sets the chunk's world
     * 
     * @param world The world
     * @since 0.1.0.0
     * @deprecated Use the constructor instead!
     */
    public void setWorld(final World world) {
        this.world = world;
    }
    
}
