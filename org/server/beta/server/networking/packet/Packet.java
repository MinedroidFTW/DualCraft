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
package dualcraft.org.server.beta.server.networking.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

/**
 * A class defining a single packet
 * 
 * 
 * @version 1.0.0.1
 */
public class Packet {
    
    /**
     * Creates a new packet
     * @param channel The channel to use
     * @param buffer The buffer to use
     */
    public Packet(Channel channel, ChannelBuffer buffer) {
        //Reset the reader index
        buffer.resetReaderIndex();
        //Get the ID
        this.packetID = buffer.readUnsignedByte();
        //Set the channel
        this.channel = channel;
        //And the buffer
        this.buffer = buffer;
    }
    
    /**
     * Creates a new packet with a dynamic buffer
     * @param channel The channel to use
     */
    public Packet(Channel channel) {
        //Set the buffer
        this.buffer = ChannelBuffers.dynamicBuffer();
        //Set the channel
        this.channel = channel;
    }
    
    /**
     * This packet's ID
     */
    private int packetID = -1;
    
    /**
     * Gets this packet's ID
     * @return The ID
     */
    public int getId() {
        //Check to see if the ID is invalid
        if (this.packetID == -1) {
            //Get the ID
            this.packetID = this.buffer.getUnsignedByte(0);
        }
        //Get the ID
        return this.packetID;        
    }
    
    /**
     * Sets this packet's ID
     * @param packetID The ID
     */
    public void setId(int packetID) {
        this.packetID = packetID;
    }
    
    /**
     * The buffer used by this packet
     */
    private ChannelBuffer buffer;
    
    /**
     * Gets the buffer used by this packet
     * @return The buffer
     */
    public ChannelBuffer getBuffer() {
        return this.buffer;
    }
    
    /**
     * Sets the buffer used by this packet
     * @param buffer The buffer
     */
    public void setBuffer(ChannelBuffer buffer) {
        this.buffer = buffer;
    }
    
    /**
     * The channel used by this packet
     */
    private Channel channel;
    
    /**
     * Gets the channel used by this packet
     * @return The channel
     */
    public Channel getChannel() {
        return this.channel;
    }
    
    /**
     * Sets the channel used by this packet
     * @param channel The channel
     */
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    
    /**
     * Writes this packet
     * @return The channel future
     */
    public ChannelFuture writePacket() {
        return this.channel.write(this.getBuffer());
    }
    
}
