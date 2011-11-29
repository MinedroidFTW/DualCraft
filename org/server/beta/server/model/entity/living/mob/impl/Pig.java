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
package dualcraft.org.server.beta.server.model.entity.living.mob.impl;

import dualcraft.org.server.beta.server.model.entity.living.mob.AbstractMob;
import dualcraft.org.server.beta.server.model.entity.living.mob.AggressionLevel;

/**
 * A class that defines a pig
 * 
 * 
 * @version 0.9.0.0
 */
public class Pig extends AbstractMob {

    /**
     * Gets the default aggression level
     * 
     * @return The default aggression level
     * @since 0.9.0.0
     */
    @Override
    public AggressionLevel getDefaultAggressionLevel() {
        return AggressionLevel.TIMID;
    }

    /**
     * Gets the mob's type
     * 
     * @return The mob's type
     * @since 0.9.0.0
     */
    @Override
    public byte getMobType() {
        return (byte) 90;
    }

    /**
     * Gets the name of this mob
     * 
     * @return The mob's name
     * @since 0.9.0.0
     */
    @Override
    public String getName() {
        return "Pig";
    }
    
    /**
     * The saddle status of this pig
     * 
     * @since 0.9.0.0
     */
    private boolean saddled;
    
    /**
     * Checks to see if this pig is saddled
     * 
     * @return True if saddled, otherwise false
     * @since 0.9.0.0
     */
    public boolean isSaddled() {
        return this.saddled;
    }
    
    /**
     * Sets the saddle status of this pig
     * 
     * @param saddled True if saddled, otherwise false
     * @since 0.9.0.0
     */
    public void setSaddled(boolean saddled) {
        this.saddled = saddled;
    }
    
}
