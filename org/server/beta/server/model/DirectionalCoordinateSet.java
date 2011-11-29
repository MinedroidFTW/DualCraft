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

import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;
import dualcraft.org.server.beta.server.nbt.impl.FloatTag;

/**
 * A directional coordinate set.
 * Adds yaw and pitch to the standard coordinate set
 * 
 * 
 * @version 1.1.0.0
 */
public class DirectionalCoordinateSet extends CoordinateSet {
    
    /**
     * The yaw of this directional coordinate set
     * 
     * @since 1.0.0.0
     */
    private float yaw = 0.0F;
    
    /**
     * Gets the yaw
     * 
     * @return The yaw
     * @since 1.0.0.0
     */
    public float getYaw() {
        return this.yaw;
    }
    
    /**
     * Gets the yaw as an integer between 0 and 255.
     * 
     * @return The yaw as an int
     * @since 1.1.0.0
     */
    public int getIntYaw() {
        return (int) (((yaw % 360) / 360) * 256);
    }
    
    /**
     * Sets the yaw
     * 
     * @param yaw The yaw to set
     * @since 1.0.0.0
     */
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    /**
     * The pitch of this directional coordinate set
     * 
     * @since 1.0.0.0
     */
    private float pitch = 0.0F;
    
    /**
     * Gets the pitch
     * 
     * @return The pitch
     * @since 1.0.0.0
     */
    public float getPitch() {
        return this.pitch;
    }
    
    /**
     * Gets the pitch as an integer between 0 and 255.
     * 
     * @return The pitch as an int
     * @since 1.1.0.0
     */
    public int getIntPitch() {
        return (int) (((pitch % 360) / 360) * 256);
    }
    
    /**
     * Sets the pitch
     * 
     * @param pitch The pitch to set
     * @since 1.0.0.0
     */
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    /**
     * Loads this class from a compound tag
     * 
     * @param tag The tag to import from
     * @since 1.0.0.0
     */
    @Override
    public void loadFromTag(final CompoundTag tag) {
        //Let the parent class handle X, Y, and Z
        super.loadFromTag(tag);
        
        //Check to see if the yaw exists
        this.yaw = tag.getContents().containsKey("yaw")
                ? (Float) (tag.getContents().get("yaw").getContents())
                : 1.0f;
        
        //Check to see if the pitch exists
        this.pitch = tag.getContents().containsKey("pitch")
                ? (Float) (tag.getContents().get("pitch").getContents())
                : 1.0f;
    }

    /**
     * Saves this class to a compound tag
     * 
     * @return The tag
     * @since 1.0.0.0
     */
    @Override
    public CompoundTag saveToTag() {
        //Get the parent tag
        final CompoundTag tag = super.saveToTag();
        
        //Set the yaw
        tag.getContents().put("yaw", new FloatTag("yaw", this.yaw));
        
        //Set the pitch
        tag.getContents().put("pitch", new FloatTag("pitch", this.pitch));
        
        //And return the new extended tag
        return tag;
        
    }
    
    
}
