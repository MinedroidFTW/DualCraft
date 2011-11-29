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
package dualcraft.org.server.beta.server.model.block.impl;

import dualcraft.org.server.beta.server.model.block.AbstractSolidBlock;

/**
 * A class that defines bedrock
 * 
 * 
 * @version 1.0.0.0
 */
public class Bedrock extends AbstractSolidBlock {

    /**
     * Returns whether this block has gravity or not (which it doesn't)
     * 
     * @return false (since there is no gravity)
     * @since 1.0.0.0
     */
    @Override
    public boolean hasGravity() {
        return false;
    }

    /**
     * Checks to see if this block is transparent
     * 
     * @return false, since bedrock is opaque
     * @since 1.0.0.0
     */
    @Override
    public boolean isTransparent() {
        return false;
    }

    /**
     * Gets the ID of this block
     * 
     * @return 7
     * @since 1.0.0.0
     */
    @Override
    public int getID() {
        return 7;
    }

    /**
     * Gets the name of this block
     * 
     * @return The name
     * @since 1.0.0.0
     */
    @Override
    public String getName() {
        return "Bedrock";
    }
    
}
