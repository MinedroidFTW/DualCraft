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
package dualcraft.org.server.beta.server.networking.packet.outgoing;

import dualcraft.org.server.beta.server.networking.netty.util.NettyStringHandler;
import dualcraft.org.server.beta.server.networking.packet.OutgoingPacket;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

/**
 * A class that defines a single kick packet
 * 
 * 
 * @version 0.9.0.0
 */
public class KickPacket extends OutgoingPacket {
    
    //TODO public KickPacket(Player player)
    
    /**
     * Creates a new kick packet
     * 
     * @param channel The channel to use     * 
     * @since 0.9.0.0
     */
    public KickPacket(Channel channel) {
        super(channel);
    }
    
    /**
     * Executes the kick
     * 
     * @since 0.9.0.0
     */
    public void executeKick() {
        this.executeKick("You were kicked");
    }
    
    /**
     * Executes the kick
     * 
     * @param message The message to send
     * @return The channel's future
     * @since 0.9.0.0
     */
    public ChannelFuture executeKick(String message) {
        
        //Set the ID
        this.getBuffer().writeByte(255);
        
        //Set the reason
        NettyStringHandler.writeUTF16(message, this.getBuffer());
        
        //And send
        return this.writePacket();
        
    }
    
}
