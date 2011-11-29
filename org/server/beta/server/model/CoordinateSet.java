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
package dualcraft.org.server.beta.server.model;

import dualcraft.org.server.beta.server.nbt.Taggable;
import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;
import dualcraft.org.server.beta.server.nbt.impl.DoubleTag;

/**
 * A class that encapsulates a coordinate set
 * 
 * 
 * @version 1.1.0.2
 */
public class CoordinateSet implements Taggable<CompoundTag> {
    
    /**
     * The X coordinate
     * 
     * @since 1.0.0.0
     */
    private double xCoordinate = 0.0D;
    
    /**
     * Gets the X coordinate
     * 
     * @return The X coordinate
     * @since 1.0.0.0
     */
    public double getXCoordinate() {
        return this.xCoordinate;
    }
    
    /**
     * Gets the X pixel coordinate
     * 
     * @return The X pixel coordinate
     * @since 1.0.0.0
     */
    public int getXPixelCoordinate() {
        return (int) (this.xCoordinate * 32.0);
    }
    
    /**
     * Sets the X coordinate
     * 
     * @param xCoordinate The X coordinate to use
     * @since 1.0.0.0
     */
    public void setXCoordinate(final double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }
    
    /**
     * Sets the X pixel coordinate
     * 
     * @param xPixelCoordinate The X pixel coordinate
     * @since 1.0.0.0
     */
    public void setXPixelCoordinate(final int xPixelCoordinate) {
        this.xCoordinate = (double) (xPixelCoordinate / 32.0);
    }
    
    /**
     * The Y coordinate
     * 
     * @since 1.0.0.0
     */
    private double yCoordinate = 0.0D;
    
    /**
     * Gets the Y coordinate
     * 
     * @return The Y coordinate
     * @since 1.0.0.0
     */
    public double getYCoordinate() {
        return this.yCoordinate;
    }
    
    /**
     * Gets the Y pixel coordinate
     * 
     * @return The Y pixel coordinate
     * @since 1.0.0.0
     */
    public int getYPixelCoordinate() {
        return (int) (this.yCoordinate * 32.0);
    }
    
    /**
     * Sets the Y coordinate
     * 
     * @param yCoordinate The Y coordinate to use
     * @since 1.0.0.0
     */
    public void setYCoordinate(final double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
    
    /**
     * Sets the Y pixel coordinate
     * 
     * @param yPixelCoordinate The Y pixel coordinate
     * @since 1.0.0.0
     */
    public void setYPixelCoordinate(final int yPixelCoordinate) {
        this.yCoordinate = (double) (yPixelCoordinate / 32.0);
    }
    
    /**
     * The Z coordinate
     * 
     * @since 1.0.0.0
     */
    private double zCoordinate = 0.0D;
    
    /**
     * Gets the Z coordinate
     * 
     * @return The Z coordinate
     * @since 1.0.0.0
     */
    public double getZCoordinate() {
        return this.zCoordinate;
    }
    
    /**
     * Gets the Z pixel coordinate
     * 
     * @return The Z pixel coordinate
     * @since 1.0.0.0
     */
    public int getZPixelCoordinate() {
        return (int) (this.zCoordinate * 32.0);
    }
    
    /**
     * Sets the Z coordinate
     * 
     * @param zCoordinate The Z coordinate to use
     * @since 1.0.0.0
     */
    public void setZCoordinate(final double zCoordinate) {
        this.zCoordinate = zCoordinate;
    }
    
    /**
     * Sets the Z pixel coordinate
     * 
     * @param zPixelCoordinate The Z pixel coordinate
     * @since 1.0.0.0
     */
    public void setZPixelCoordinate(final int zPixelCoordinate) {
        this.zCoordinate = (double) (zPixelCoordinate / 32.0);
    }

    /**
     * Loads this class from a compound tag
     * 
     * @param tag The compound tag to import from
     * @since 1.1.0.0
     */
    @Override
    public void loadFromTag(final CompoundTag tag) {
        
        //Check to see if the X coordinate exists
        this.xCoordinate = tag.getContents().containsKey("x")
                ? (Double) (tag.getContents().get("x").getContents())
                : 1.0;
        
        //Check to see if the Y coordinate exists
        this.yCoordinate = tag.getContents().containsKey("y")
                ? (Double) (tag.getContents().get("y").getContents())
                : 1.0;
        
        //Check to see if the Z coordinate exists
        this.zCoordinate = tag.getContents().containsKey("z")
                ? (Double) (tag.getContents().get("z").getContents())
                : 1.0;
    }

    /**
     * Saves this class to a compound tag
     * 
     * @return The compound tag
     * @since 1.1.0.0
     */
    @Override
    public CompoundTag saveToTag() {
        //Set up the compound tag
        final CompoundTag tag = new CompoundTag();
        
        //Add the X coordinate
        tag.getContents().put("x", new DoubleTag("x", this.xCoordinate));
        
        //Add the Y coordinate
        tag.getContents().put("y", new DoubleTag("y", this.yCoordinate));
        
        //Add the Z coordinate
        tag.getContents().put("z", new DoubleTag("z", this.zCoordinate));
        
        //And return the tag
        return tag;
    }
    
}
