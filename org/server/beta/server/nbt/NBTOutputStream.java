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

import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;
import dualcraft.org.server.beta.server.nbt.impl.EndTag;
import dualcraft.org.server.beta.server.nbt.impl.ListTag;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * A single class that defines a NBT output stream
 * 
 * 
 * @version 1.0.0.1
 */
public class NBTOutputStream implements Closeable {
    
    /**
     * The data output stream
     * 
     * @since 0.9.0.0
     */
    private final DataOutputStream stream;
    
    /**
     * Gets the output stream
     * 
     * @return The output stream
     * @since 0.9.0.0
     */
    public DataOutputStream getStream() {
        return this.stream;
    }
    
    /**
     * Creates a new Named Binary Tag output stream
     * 
     * @param stream The output stream
     * @param useGzip Whether gzip is used
     * @throws IOException Can't open stream
     * @since 0.9.0.0
     */
    public NBTOutputStream(OutputStream stream, boolean useGzip) throws IOException {
        this.stream = useGzip ? new DataOutputStream(new GZIPOutputStream(stream)) : new DataOutputStream(stream);
    }

    /**
     * Closes the output stream
     * 
     * @throws IOException Can't close stream
     * @since 0.9.0.0
     */
    @Override
    public void close() throws IOException {
        this.stream.close();
    }
    
    /**
     * Writes a tag
     * 
     * @param tag The tag to write
     * @throws IOException Could not access file
     * @throws IllegalTagException Unexpected tag
     * @since 1.0.0.0
     */
    public void writeTag(AbstractTag tag) throws IOException, IllegalTagException {
        
        //Write the ID
        this.stream.writeByte(tag.getType().getId());
        
        //Do a quick check
        if (tag.getType() == TagType.End) {
            //We don't write any more
            return;
        }
        
        //Set up the name bytes
        byte[] nameBytes = tag.getName().getBytes(Charset.forName("UTF-8"));
        
        //Write the length of the name
        this.stream.writeShort(nameBytes.length);
        
        //And wite the bytes
        this.stream.write(nameBytes);
        
        //Now write the details
        this.writeTagDetails(tag);
        
    }
    
    /**
     * Writes a tag's details
     * 
     * @param tag The tag to write
     * @throws IOException Could not access file
     * @throws IllegalTagException Unexpected tag
     * @since 1.0.0.0
     */
    private void writeTagDetails(AbstractTag tag) throws IOException, IllegalTagException {
        
        //Check to see if it's an end tag
        if (tag.getType() == TagType.End) {
            //Throw an exception
            throw new IllegalTagException("End Tag being written in writeTagDetails");
        }
        
        //Check to see if it's a byte tag
        if (tag.getType() == TagType.Byte) {
            //Write a byte
            this.stream.writeByte((Byte) tag.getContents());
            return;
        }
        
        //Check to see if it's a short tag
        if (tag.getType() == TagType.Short) {
            //Write a short
            this.stream.writeShort((Short) tag.getContents());
            return;
        }
        
        //Check to see if it's an int tag
        if (tag.getType() == TagType.Int) {
            //Write an int
            this.stream.writeInt((Integer) tag.getContents());
            return;
        }
        
        //Check to see if it's a long tag
        if (tag.getType() == TagType.Long) {
            //Write a long
            this.stream.writeLong((Long) tag.getContents());
            return;
        }
        
        //Check to see if it's a float tag
        if (tag.getType() == TagType.Float) {
            //Write a float
            this.stream.writeFloat((Float) tag.getContents());
            return;
        }
        
        //Check to see if it's a double tag
        if (tag.getType() == TagType.Double) {
            //Write a double
            this.stream.writeDouble((Double) tag.getContents());
            return;
        }
        
        //Check to see if it's a byte array
        if (tag.getType() == TagType.ByteArray) {
            //Get the bytes
            byte[] bytes = (byte[]) tag.getContents();
            //Write the length of the array
            this.stream.writeInt(bytes.length);
            //And write the bytes
            this.stream.write(bytes);
            //Oh, and return
            return;
        }
        
        //Check to see if it's a string
        if (tag.getType() == TagType.String) {
            //Get the bytes
            byte[] bytes = ((String) tag.getContents()).getBytes("UTF-8");
            //Write the length of the array
            this.stream.writeShort(bytes.length);
            //And write the bytes
            this.stream.write(bytes);
            //Oh, and return
            return;
        }
        
        //Check to see if it's a list
        if (tag.getType() == TagType.List) {
                       
            //Set up the tag list
            final List<AbstractTag> listTags = ((List<AbstractTag>) tag.getContents());
            
            //And write the type
            this.stream.writeByte(((ListTag)tag).getListTagType().getId());
            
            //And write the length
            this.stream.writeInt(listTags.size());
            
            //Start going loopy
            for (AbstractTag listInternalTag : listTags) {
                //And write the details
                this.writeTagDetails(listInternalTag);
            }
            
            return;
            
        }
        
        //Check to see if it's a compound
        if (tag.getType().equals(TagType.Compound)) {
            //Get the tag map
            final Map<String, AbstractTag> tags = ((CompoundTag)tag).getContents();
            //Start looping
            for (AbstractTag _tag : tags.values()) {
                //Write the tag
                this.writeTag(_tag);
            }
            //And write the end tag
            this.writeTag(new EndTag());
            //Finally return
            return;
        }
        
        throw new IllegalTagException("Unwritable tag " + tag.getName() + " being written");
        
    }
    
}
