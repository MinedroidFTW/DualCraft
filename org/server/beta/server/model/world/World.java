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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A single class that defines a world
 * 
 * 
 * @version 0.1.0.0
 */
public class World {
    
    /**
     * Creates a new named world
     * 
     * @param name The name to use
     * @since 0.1.0.0
     */
    public World(String name) {
        this.name = name;
    }
    
    /**
     * The name of this world
     * 
     * @since 0.0.0.1
     */
    private String name = "";
    
    /**
     * Gets the name of this world
     * 
     * @return The name
     * @since 0.0.0.1
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the name of this world
     * 
     * @param name The name to set
     * @since 0.0.0.1
     */
    public void setName(final String name) {
        this.name = name;
    }
    
    /**
     * The map of chunks
     * 
     * @since 0.1.0.0
     */
    private transient Map<ChunkLocation, Chunk> chunks = new HashMap<ChunkLocation, Chunk>();
    
    /**
     * Gets the chunks
     * 
     * @return The chunks
     * @since 0.1.0.0
     */
    public Map<ChunkLocation, Chunk> getChunks() {
        return this.chunks;
    }
    
    /**
     * Gets the chunk at the specified coordinates
     * 
     * @param chunkX The X coordinate
     * @param chunkZ The Z coordinate
     * @return The chunk
     * @since 0.1.0.0
     */
    public Chunk getChunkAt(final int chunkX, final int chunkZ) {
        //Get the chunk location
        final ChunkLocation location = new ChunkLocation(chunkX, chunkZ);
        //Check for a chunk
        if (this.chunks.containsKey(location)) {
            //Return the chunk
            return this.chunks.get(location);
        } else {
            //TODO Generate chunk
            Logger.getLogger("World").warning("Can't get chunk " + chunkX +"," + chunkZ);
            return null;
        }
    }
    
    /**
     * Gets the chunk at the specified location
     * 
     * @param location The chunk location
     * @return The chunk
     * @since 0.1.0.0
     */
    public Chunk getChunkAt(final ChunkLocation location) {
        //Check for a chunk
        if (this.chunks.containsKey(location)) {
            //Return the chunk
            return this.chunks.get(location);
        } else {
            //TODO Generate chunk
            Logger.getLogger("World").warning("Can't get chunk " + location.getX() +"," + location.getZ());
            return null;
        }
    }
    
}
