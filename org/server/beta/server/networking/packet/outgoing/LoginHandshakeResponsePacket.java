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

/**
 * An outgoing packet that encapsulates a login handshake response
 * 
 * 
 * @version 1.0.0.0
 */
public class LoginHandshakeResponsePacket extends OutgoingPacket {
    
    /**
     * Creates a new login handshake response packet
     * 
     * @param channel The channel to use
     * @since 1.0.0.0
     */
    public LoginHandshakeResponsePacket(Channel channel) {
        super(channel);
    }
    
    /**
     * Sends the handshake
     * 
     * @param responseString The response string to include
     * @since 1.0.0.0
     */
    public void sendHandshake(String responseString) {
        //First set the ID
        this.getBuffer().writeByte(2);
        
        //Write the response string
        NettyStringHandler.writeUTF16(responseString, this.getBuffer());
        
        //Write the packet
        this.writePacket();
        
    }
    
}

