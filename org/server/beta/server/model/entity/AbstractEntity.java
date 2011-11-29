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
package dualcraft.org.server.beta.server.model.entity;

import dualcraft.org.server.beta.server.model.Universe;
import dualcraft.org.server.beta.server.nbt.Taggable;
import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;

/**
 * An abstract class that defines a single entity
 * 
 * 
 * @version 0.2.1.0
 */
public abstract class AbstractEntity implements Taggable<CompoundTag> {
    
    /**
     * Creates a new entity
     * 
     * @since 0.1.0.0
     */
    public AbstractEntity() {
        //Set the entity's ID
        this.entityID = Universe.getInstance().getNextEntityID();
    }
    
    /**
     * The entity's ID
     * 
     * @since 0.1.0.0
     */
    private transient final int entityID;
    
    /**
     * Get the entity's ID
     * 
     * @return The ID
     * @since 0.1.0.0
     */
    public int getEntityID() {
        return this.entityID;
    }
    
    /**
     * Gets the name of this entity
     * 
     * @return The name
     * @since 0.2.0.0
     */
    public abstract String getName();
   
}
