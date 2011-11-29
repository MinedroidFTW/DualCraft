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

import dualcraft.org.server.beta.server.nbt.impl.*;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * A single class that defines a NBT input stream
 *
 * 
 * @version 1.0.0.0
 */
public class NBTInputStream implements Closeable {

    /**
     * The data input stream
     *
     * @since 0.9.0.0
     */
    private final DataInputStream stream;

    /**
     * Gets the input stream
     * 
     * @return The input stream
     * @since 0.9.0.0
     */
    public DataInputStream getStream() {
        return this.stream;
    }

    /**
     * Creates a new Named Binary Tag input stream
     * 
     * @param stream The input stream
     * @param useGzip Whether gzip is used
     * @throws IOException Can't open stream
     * @since 0.9.0.0
     */
    public NBTInputStream(InputStream stream, boolean useGzip) throws IOException {
        this.stream = useGzip ? new DataInputStream(new GZIPInputStream(stream)) : new DataInputStream(stream);
    }

    /**
     * Closes the input stream
     * 
     * @throws IOException Can't close stream
     * @since 0.9.0.0
     */
    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    /**
     * Reads a tag at depth 0
     * 
     * @return The tag
     * @throws IOException An error occurred while reading the tag
     * @throws IllegalTagException An illegal tag was found (not allowed in that area)
     * @throws TagTypeNotFoundException The tag type loaded is not available
     * @since 1.0.0.0
     */
    public AbstractTag readTag() throws IOException, TagTypeNotFoundException, IllegalTagException {
        return this.readTag(0);
    }

    /**
     * Reads a tag 
     * 
     * @param tagDepth The depth of the tag
     * @throws IOException An error occurred while reading the tag
     * @throws IllegalTagException An illegal tag was found (not allowed in that area)
     * @throws TagTypeNotFoundException The tag type loaded is not available
     * @return The tag
     * @since 1.0.0.0
     */
    public AbstractTag readTag(int tagDepth) throws IOException, TagTypeNotFoundException, IllegalTagException {

        //Set up the tag type
        TagType tagType = TagType.forID(this.stream.readByte() & 0xff);

        //And the name
        String tagName = "";

        //Check to see if it's an end tag
        if (tagType.equals(TagType.End)) {
            //Check to see if the tag is legal
            if (tagDepth == 0) {
                throw new IllegalTagException("EndTag found outside of a list/compound tag");
            }
            //It's an end tag, so we don't read anything else!
            return new EndTag();
        }

        //Get the number of bytes to read for the name
        int nameBytesAmount = this.stream.readShort() & 0xFFFF;

        //Set up the byte array for the name
        byte[] nameBytes = new byte[nameBytesAmount];


        //Read the name bytes
        this.stream.readFully(nameBytes);

        //And set up the name
        tagName = new String(nameBytes, Charset.forName("UTF-8"));

        //Get the tag details and return
        return getTagDetails(tagDepth, tagType, tagName);
    }

    /**
     * Reads a tag's details
     * 
     * @param tagDepth The depth of the tag
     * @param tagType The type of tag
     * @param tagName The tag's name
     * @throws IOException An error occurred while reading the tag
     * @throws IllegalTagException An illegal tag was found (not allowed in that area)
     * @throws TagTypeNotFoundException The tag type loaded is not available
     * @return The tag
     * @since 1.0.0.0
     */
    private AbstractTag getTagDetails(int tagDepth, TagType tagType, String tagName) throws IOException, TagTypeNotFoundException, IllegalTagException {
        
        //First check to see if it's an end tag
        if (tagType == TagType.End) {
            //Do a sanity check
            if (tagDepth == 0) {
                throw new IllegalTagException("Orphaned End tag found");
            }
            //And return
            return new EndTag();
        }
        
        //We check to see if it's a byte tag
        if (tagType == TagType.Byte) {
            //Return a byte tag
            return new ByteTag(tagName, this.stream.readByte());
        }

        //Now check to see if it's a short tag
        if (tagType == TagType.Short) {
            //Return a short tag
            return new ShortTag(tagName, this.stream.readShort());
        }

        //Now check to see if it's an int tag
        if (tagType == TagType.Int) {
            //Return an int tag
            return new IntTag(tagName, this.stream.readInt());
        }

        //Now check to see if it's a long tag
        if (tagType == TagType.Long) {
            //Return a long tag
            return new LongTag(tagName, this.stream.readLong());
        }

        //Check to see if it's a float tag
        if (tagType == TagType.Float) {
            //Return a new float tag
            return new FloatTag(tagName, this.stream.readFloat());
        }

        //Check to see if it's a double tag
        if (tagType == TagType.Double) {
            //Return a new double tag
            return new DoubleTag(tagName, this.stream.readDouble());
        }

        //Check to see if it's a byte array tag
        if (tagType == TagType.ByteArray) {
            //Set up a temporary payload
            byte[] payload = new byte[this.stream.readInt()];
            //Fill the payload
            this.stream.readFully(payload);
            //And return a byte array tag
            return new ByteArrayTag(tagName, payload);
        }

        //Check to see if it's a string tag
        if (tagType == TagType.String) {
            //Set up the payload
            byte[] payload = new byte[this.stream.readShort()];
            //And read it
            this.stream.readFully(payload);
            //Return a new string tag
            return new StringTag(tagName, new String(payload, Charset.forName("UTF-8")));
        }

        //Check to see if it's a list tag
        if (tagType == TagType.List) {
            //Get the new tag type to use
            TagType newTagType = TagType.forID(this.stream.readByte());
            //Set up a new tag list
            List<AbstractTag> tags = new ArrayList<AbstractTag>();
            //Get the length
            int length = this.stream.readInt();
            //Start going loopy
            for (int index = 0; index < length; index++) {
                //Get the tag
                AbstractTag tag = this.getTagDetails(tagDepth, newTagType, tagName);
                //Do a quick sanity check
                if (tag.getType() != newTagType) {
                    throw new IllegalTagException("Tag " + tag.getType().getName() + " found in a list where a " + newTagType.getName() + " tag was expected");
                }
                //Add the tag
                tags.add(tag);
            }
            return new ListTag(tagName, newTagType, tags);
        }
        
        //Check to see if it's a compound tag
        if (tagType == TagType.Compound) {
            //Initialize the tag map
            LinkedHashMap<String, AbstractTag> tags = new LinkedHashMap<String, AbstractTag>();
            
            //Start looping
            while (this.stream.available() > 0) {
                //Get the tag
                AbstractTag newTag = this.readTag(tagDepth + 1);
                //Check to see if it's an end tag
                if (newTag.getType() == TagType.End) {
                    //Return a compound tag
                    return new CompoundTag(tagName, tags);
                }
                //Not the end, add the tag
                tags.put(newTag.getName(), newTag);
            }
            
        }

        
        //Haha, you're using a fake tag!
        throw new IllegalTagException("Tag type " + tagType.getName() + " can't be parsed at this time"); 

    }
}
