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
package dualcraft.org.server.beta.server.model.block;

/**
 * An abstract class that defines a single abstract block
 * 
 * 
 * @version 0.3.0.0
 */
public abstract class AbstractBlock {
    
    /**
     * Gets the ID of this block
     * 
     * @return The ID
     * @since 0.1.0.0
     */
    public abstract int getID();
    
    /**
     * Gets the name of this block
     * 
     * @return The name
     * @since 0.1.0.0
     */
    public abstract String getName();
    
    /**
     * The X coordinate
     * 
     * @since 0.1.0.0
     */
    private int xCoordinate = 0;
    
    /**
     * Gets the X coordinate
     * 
     * @return The X coordinate
     * @since 0.1.0.0
     */
    public int getXCoordinate() {
        return this.xCoordinate;
    }
    
    /**
     * Gets the chunk's X coordinate
     * 
     * @return The chunk's X coordinate
     * @since 0.2.0.0
     */
    public int getChunkXCoordinate() {
        return this.xCoordinate >> 4;
    }
    
    /**
     * Gets the local X coordinate
     * 
     * @return The local X coordinate
     * @since 0.2.0.0
     */
    public int getLocalXCoordinate() {
        return this.xCoordinate & 15;
    }
    
    /**
     * Sets the X coordinate
     * 
     * @param xCoordinate The X coordinate to set
     * @since 0.1.0.0
     */
    public void setXCoordinate(final int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    
    /**
     * The Y coordinate
     * 
     * @since 0.1.0.0
     */
    private int yCoordinate = 0;
    
    /**
     * Gets the Y coordinate
     * 
     * @return The Y coordinate
     * @since 0.1.0.0
     */
    public int getYCoordinate() {
        return this.yCoordinate;
    }
    
    /**
     * Gets the chunk's Y coordinate
     * 
     * @return The chunk's Y coordinate
     * @since 0.2.0.0
     */
    public int getChunkYCoordinate() {
        return this.yCoordinate >> 7;
    }
    
    /**
     * Gets the local Y coordinate
     * 
     * @return The local Y coordinate
     * @since 0.2.0.0
     */
    public int getLocalYCoordinate() {
        return this.yCoordinate & 127;
    }
    
    /**
     * Sets the Y coordinate
     * 
     * @param yCoordinate The Y coordinate to set
     * @since 0.1.0.0
     */
    public void setYCoordinate(final int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    
    /**
     * The Z coordinate
     * 
     * @since 0.1.0.0
     */
    private int zCoordinate = 0;
    
    /**
     * Gets the Z coordinate
     * 
     * @return The Z coordinate
     * @since 0.1.0.0
     */
    public int getZCoordinate() {
        return this.zCoordinate;
    }
    
    /**
     * Gets the chunk's Z coordinate
     * 
     * @return The chunk's Z coordinate
     * @since 0.2.0.0
     */
    public int getChunkZCoordinate() {
        return this.zCoordinate >> 4;
    }
    
    /**
     * Gets the local Z coordinate
     * 
     * @return The local Z coordinate
     * @since 0.2.0.0
     */
    public int getLocalZCoordinate() {
        return this.zCoordinate & 15;
    }
    
    /**
     * Sets the Z coordinate
     * 
     * @param zCoordinate The Z coordinate to set
     * @since 0.1.0.0
     */
    public void setZCoordinate(final int zCoordinate) {
        this.zCoordinate = zCoordinate;
    }
    
    /**
     * The block light level of this block.
     * This light level is defined by surrounding light-emitting blocks
     * 
     * @since 0.3.0.0
     */
    private int blockLightLevel = 0;
    
    /**
     * Sets the block light level of this block
     * 
     * @param blockLightLevel The block light level to use
     * @since 0.3.0.0
     */
    public void setBlockLightLevel(final int blockLightLevel) {
        //Check to see if the light level is too high
        if (blockLightLevel > 15) {
            //Reset it to 15.
            this.blockLightLevel = 15;
        } 
        //Now check to see if the light level is less than 0
        else if (blockLightLevel < 0) {
            //Set it to 0
            this.blockLightLevel = 0;
        } else {
            //Set the block light level
            this.blockLightLevel = blockLightLevel;
        }
    }
    
    /**
     * Gets the block's light level
     * 
     * @return The light level
     * @since 0.3.0.0
     */
    public int getBlockLightLevel() {
        return this.blockLightLevel;
    }
    
    /**
     * The sun light level of this block.
     * This light level is defined by the sun
     * 
     * @since 0.3.0.0
     */
    private int sunLightLevel = 15;
    
    /**
     * Sets the block light level of this block
     * 
     * @param sunLightLevel The block light level to use
     * @since 0.3.0.0
     */
    public void setSunLightLevel(final int sunLightLevel) {
        //Check to see if the light level is too high
        if (sunLightLevel > 15) {
            //Reset it to 15.
            this.sunLightLevel = 15;
        } 
        //Now check to see if the light level is less than 0
        else if (sunLightLevel < 0) {
            //Set it to 0
            this.sunLightLevel = 0;
        } else {
            //Set the block light level
            this.sunLightLevel = sunLightLevel;
        }
    }
    
    /**
     * Gets the block's sunlight level
     * 
     * @return The light level
     * @since 0.3.0.0
     */
    public int getSunLightLevel() {
        return this.sunLightLevel;
    }
    
}
