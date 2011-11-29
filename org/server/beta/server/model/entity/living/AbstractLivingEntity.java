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
package dualcraft.org.server.beta.server.model.entity.living;

import dualcraft.org.server.beta.server.model.entity.AbstractEntity;
import dualcraft.org.server.beta.server.model.DirectionalCoordinateSet;
import dualcraft.org.server.beta.server.nbt.impl.ByteTag;
import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;

/**
 * An abstract class defining a living entity
 * 
 * 
 * @version 0.2.0.2
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    
    /**
     * The coordinates for this entity
     * 
     * @since 0.2.0.2
     */
    private final DirectionalCoordinateSet coordinates = new DirectionalCoordinateSet();
    
    /**
     * Gets the coordinates for this entity
     * 
     * @return The coordinates
     * @since 0.2.0.2
     */
    public DirectionalCoordinateSet getCoordinates() {
        return this.coordinates;
    }
    
    /**
     * Defines if the entity is on fire
     * 
     * @since 0.2.0.0
     */
    private boolean onFire = false;
    
    /**
     * Checks to see if the entity is on fire
     * 
     * @return True if on fire, otherwise false
     * @since 0.2.0.0
     */
    public boolean isOnFire() {
        return this.onFire;
    }
    
    /**
     * Sets the fire status of this entity
     * 
     * @param onFire True if on fire, otherwise false
     * @since 0.2.0.0
     */
    public void setOnFire(final boolean onFire) {
        this.onFire = onFire;
    }
    
    /**
     * Defines if the entity is crouching
     * 
     * @since 0.2.0.0
     */
    private boolean crouching = false;
    
    /**
     * Checks to see if the entity is crouching
     * 
     * @return True if crouching, otherwise false
     * @since 0.2.0.0
     */
    public boolean isCrouching() {
        return this.crouching;
    }
    
    /**
     * Sets the crouch status of this entity
     * 
     * @param isCrouching True if crouching, otherwise false
     * @since 0.2.0.0
     */
    public void setCrouching(final boolean isCrouching) {
        this.crouching = isCrouching;
    }
    
    /**
     * Defines if the entity is riding
     * 
     * @since 0.2.0.0
     */
    private boolean riding = false;
    
    /**
     * Checks to see if the entity is riding
     * 
     * @return True if riding, otherwise false
     * @since 0.2.0.0
     */
    public boolean isRiding() {
        return this.riding;
    }
    
    /**
     * Sets the riding status of this entity
     * 
     * @param isRiding True if riding, otherwise false
     * @since 0.2.0.0
     */
    public void setRiding(final boolean isRiding) {
        this.riding = isRiding;
    }
    
    /**
     * Loads this class from a tag
     * 
     * @param tag The compound tag to load from
     * @since 0.2.1.0
     */
    @Override
    public void loadFromTag(final CompoundTag tag) {
        //Check to see if the coordinates exist
        if (tag.getContents().containsKey("coordinates")) {
            this.coordinates.loadFromTag((CompoundTag) tag.getContents().get("coordinates"));
        }
        
        //Check to see if the entity is on fire
        this.onFire = tag.getContents().containsKey("onFire")
                ? ((Byte) (tag.getContents().get("onFire").getContents()) == 1)
                : false;
        
        //Check to see if the entity is riding
        this.riding = tag.getContents().containsKey("riding")
                ? ((Byte) (tag.getContents().get("riding").getContents()) == 1)
                : false;
    }

    /**
     * Saves this class as a tag
     * 
     * @return The compound tag to save to
     * @since 0.2.1.0
     */
    @Override
    public CompoundTag saveToTag() {
        //Set up the compound tag
        final CompoundTag tag = new CompoundTag();
        //Set the coordinates
        tag.getContents().put("coordinates", this.coordinates.saveToTag());
        //Set the onFire status
        tag.getContents().put("onFire", new ByteTag("onFire", this.onFire ? (byte) 1 : (byte) 0));
        //Set the riding status
        tag.getContents().put("riding", new ByteTag("riding", this.riding ? (byte) 1 : (byte) 0));
    
        //Return the tag
        return tag;
    }
    
}
