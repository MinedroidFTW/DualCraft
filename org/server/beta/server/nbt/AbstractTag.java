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
package dualcraft.org.server.beta.server.nbt;

/**
 * An abstract class that defines a single tag type
 * 
 * @param <T> The type associated with this tag
 * 
 * @version 1.0.0.1
 */
public abstract class AbstractTag<T> {
    
    /**
     * Creates a new tag
     * 
     * @param name The name
     * @param contents The contents
     */
    public AbstractTag(final String name, final T contents) {
        this.name = name;
        this.contents = contents;
    }
    
    /**
     * Creates a new tag
     * 
     * @param contents The contents
     */
    public AbstractTag(final T contents) {
        this.name = "";
        this.contents = contents;
    }
    
    /**
     * Gets the tag type of this tag
     * 
     * @return The tag type
     * @since 1.0.0.0
     */
    public abstract TagType getType();
    
    /**
     * The contents of this tag
     * 
     * @since 1.0.0.0
     */
    private T contents;
    
    /**
     * Gets the contents of this tag
     * 
     * @return The contents
     * @since 1.0.0.0
     */
    public T getContents() {
        return this.contents;
    }
    
    /**
     * Sets the contents of this tag
     * 
     * @param contents The contents
     * @since 1.0.0.0
     */
    public void setContents(final T contents) {
        this.contents = contents;
    }
    
    /**
     * The name of this tag
     * 
     * @since 1.0.0.0
     */
    private String name;
    
    /**
     * Gets the name of this tag
     * 
     * @return The name
     * @since 1.0.0.0
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the name of this tag
     * 
     * @param name The name
     * @since 1.0.0.0
     */
    public void setName(final String name) {
        this.name = name;
    }
    
}
