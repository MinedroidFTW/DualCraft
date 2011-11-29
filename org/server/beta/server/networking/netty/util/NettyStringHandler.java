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
package dualcraft.org.server.beta.server.networking.netty.util;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A class that reads and writes Minecraft strings to/from Netty
 * 
 * 
 * @version 1.0.0.1
 */
public final class NettyStringHandler {
    
    /**
     * Not used.
     * 
     * @since 1.0.0.1
     */
    private NettyStringHandler() { }
        
    /**
     * Writes a UTF-16 string to the buffer
     * 
     * @param string The string
     * @param buffer The buffer
     * @since 0.7.0.0
     */
    public static void writeUTF16(final String string, final ChannelBuffer buffer) {
        
        //Get the character array
        final char[] characters = string.toCharArray();
        
        //Write the length
        buffer.writeShort(characters.length);
        
        //Loop through each character
        for (char character : characters) {
            //And write the character /fp
            buffer.writeShort(character);
        }
    }
    
    /**
     * Writes a UTF-8 string to the buffer
     * 
     * @param string The string
     * @param buffer The buffer
     * @since 0.8.0.0
     */
    public static void writeUTF8(final String string, final ChannelBuffer buffer) {
        
        //Get the character array
        final char[] characters = string.toCharArray();
        
        //Write the length
        buffer.writeShort(characters.length);
        
        //Loop through each character
        for (char character : characters) {
            //And write the character /fp
            buffer.writeByte(character);
        }
    }
    
    /**
     * Reads a UTF-16 string
     * 
     * @param buffer The buffer to use
     * @return The string to return
     * @since 0.7.0.0
     */
    public static String readUTF16(final ChannelBuffer buffer) {
        
        //Get the length
        final int length = buffer.readShort();
        
        //Set up a character array
        char[] characters = new char[length];
        
        //Start looping
        for (int index = 0; index < length; index++) {
            //Set the next character
            characters[index] = (char) buffer.readShort();
        }
        
        //Return the string value
        return String.valueOf(characters);        
    }
    
    /**
     * Reads a UTF-8 string
     * 
     * @param buffer The buffer to use
     * @return The string to return
     * @since 0.9.0.0
     */
    public static String readUTF8(final ChannelBuffer buffer) {
        
        //Get the length
        final int length = buffer.readShort();
        
        //Set up a character array
        char[] characters = new char[length];
        
        //Start looping
        for (int index = 0; index < length; index++) {
            //Set the next character
            characters[index] = (char) buffer.readUnsignedByte();
        }
        
        //Return the string value
        return String.valueOf(characters);        
    }
    
}
