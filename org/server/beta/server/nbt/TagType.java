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
 * A class that defines tag types
 *
 * 
 * @version 1.1.0.2
 */
public class TagType {

    /**
     * The type of the End tag
     *
     * @since 1.0.0.0
     */
    public static final TagType End = new TagType(0, "End");
    
    /**
     * The type of the Byte tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Byte = new TagType(1, "Byte");
    
    /**
     * The type of the Short tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Short = new TagType(2, "Short");
    
    /**
     * The type of the Int tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Int = new TagType(3, "Int");
    
    /**
     * The type of the Long tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Long = new TagType(4, "Long");
    
    /**
     * The type of the Float tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Float = new TagType(5, "Float");
    
    /**
     * The type of the Double tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Double = new TagType(6, "Double");
    
    /**
     * The type of the ByteArray tag
     *
     * @since 1.0.0.0
     */
    public static final TagType ByteArray = new TagType(7, "ByteArray");
    
    /**
     * The type of the String tag
     *
     * @since 1.0.0.0
     */
    public static final TagType String = new TagType(8, "String");
    
    /**
     * The type of the List tag
     *
     * @since 1.0.0.0
     */
    public static final TagType List = new TagType(9, "List");
    
    /**
     * The type of the Compound tag
     *
     * @since 1.0.0.0
     */
    public static final TagType Compound = new TagType(10, "Compound");

    /**
     * Gets an array of tag types
     * 
     * @return The tag types
     * @since 1.0.0.0
     */
    public static TagType[] getTagTypes() {
        return new TagType[] { 
            End, Byte, Short,
            Int, Long, Float,
            Double, ByteArray,
            String, List, Compound};
    }
    
    /**
     * Gets a tag type that matches the ID specified
     * 
     * @param typeID The ID to find
     * @return The tag type found
     * @throws TagTypeNotFoundException The tag type was not found
     * @since 1.0.0.1
     */
    public static TagType forID(int typeID) throws TagTypeNotFoundException {
        //Get each tag type
        for(TagType type : getTagTypes()) {
            //Check the ID
            if (type.getId() == typeID) {
                return type; //return
            }
        }
        
        //No type? Throw an exception
        throw new TagTypeNotFoundException("Tag type for " + typeID + " not found");
    }

    /**
     * Creates a new tag type
     * 
     * @param typeID The ID to use
     * @param name The name to use
     * @since 1.0.0.0
     */
    public TagType(int typeID, String name) {
        //Set the ID
        this.typeID = typeID;
        //Set the name
        this.name = name;
    }
    
    /**
     * The name of this tag type
     * 
     * @since 1.1.0.0
     */
    private String name;
    
    /**
     * Gets the name of this tag type
     * 
     * @return The name
     * @since 1.1.0.0
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * The ID of this tag
     *
     * @since 1.0.0.0
     */
    private int typeID;

    /**
     * Gets the ID of this tag
     * 
     * @return The ID
     * @since 1.0.0.0
     */
    public int getId() {
        return this.typeID;
    }
}
