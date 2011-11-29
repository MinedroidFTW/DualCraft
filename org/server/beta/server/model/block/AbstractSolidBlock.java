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
 * An abstract class that defines a single block
 * 
 * 
 * @version 0.1.0.0
 */
public abstract class AbstractSolidBlock extends AbstractBlock {
    
    /**
     * Checks to see if the solid block has gravity (AKA if it falls down)
     * 
     * @return True if gravity is to be applied
     * @since 1.1.0.0
     */
    public abstract boolean hasGravity();
    
    /**
     * Checks to see if the solid block is transparent (AKA if it is glass)
     * 
     * @return True if the block is transparent
     * @since 1.1.0.0
     */
    public abstract boolean isTransparent();
    
}
