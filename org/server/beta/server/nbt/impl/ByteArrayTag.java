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
package dualcraft.org.server.beta.server.nbt.impl;

import dualcraft.org.server.beta.server.nbt.AbstractTag;
import dualcraft.org.server.beta.server.nbt.TagType;

/**
 * A class that defines a ByteArray tag
 * 
 * 
 * @version 1.0.0.2
 */
public class ByteArrayTag extends AbstractTag<byte[]> {
    
    /**
     * Creates a new ByteArray tag 
     * 
     * @param name The name to use
     * @param contents The contents to use
     * @since 1.0.0.0
     */
    public ByteArrayTag(final String name, final byte[] contents) {
        super(name, contents);
    }
    
    /**
     * Creates a new ByteArray tag
     * 
     * @param contents The contents to use
     * @since 1.0.0.0
     */
    public ByteArrayTag(final byte[] contents) {
        super(contents);
    }

    /**
     * Gets the type of this tag
     * 
     * @return The type
     * @since 1.0.0.0
     */
    @Override
    public TagType getType() {
        return TagType.ByteArray;
    }
    
}
