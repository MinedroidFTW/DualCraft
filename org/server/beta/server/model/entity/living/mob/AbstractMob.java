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
package dualcraft.org.server.beta.server.model.entity.living.mob;

import dualcraft.org.server.beta.server.model.entity.living.AbstractLivingEntity;

/**
 * An abstract class that defines a single mob
 * 
 * 
 * @version 0.1.0.2
 */
public abstract class AbstractMob extends AbstractLivingEntity {
    
    /**
     * The aggression level of this mob
     * 
     * @since 0.1.0.2
     */
    private AggressionLevel aggressionLevel;
    
    /**
     * Gets the aggression level of this mob
     * 
     * @return The aggression level
     * @since 0.1.0.0
     */
    public AggressionLevel getAggressionLevel() {
        return this.aggressionLevel;
    }
    
    /**
     * Sets the aggression level of this mob
     * 
     * @param aggressionLevel The aggression level to set
     * @since 0.1.0.2
     */
    public void setAggressionLevel(AggressionLevel aggressionLevel) {
        this.aggressionLevel = aggressionLevel;
    }
    
    /**
     * Gets the default aggression level for this mob
     * 
     * @return The default aggression level
     * @since 0.1.0.2
     */
    public abstract AggressionLevel getDefaultAggressionLevel();
    
    /**
     * Gets the mob's type
     * 
     * @return The mob type byte
     * @since 0.1.0.1
     */
    public abstract byte getMobType();
    
}
