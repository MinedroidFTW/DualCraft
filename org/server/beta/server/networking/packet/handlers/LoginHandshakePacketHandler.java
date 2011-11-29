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
package dualcraft.org.server.beta.server.networking.packet.handlers;

import dualcraft.org.server.beta.server.model.Universe;
import dualcraft.org.server.beta.server.networking.netty.util.NettyStringHandler;
import dualcraft.org.server.beta.server.networking.packet.AbstractPacketHandler;
import dualcraft.org.server.beta.server.networking.packet.Packet;
import dualcraft.org.server.beta.server.networking.packet.outgoing.LoginHandshakeResponsePacket;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A packet handler that handles the login handshake 
 * 
 * 
 * @version 1.0.0.1
 */
public class LoginHandshakePacketHandler extends AbstractPacketHandler {

    /**
     * Gets the name of this packet handler
     * 
     * @since 1.0.0.0
     */
    @Override
    public String getName() {
        return "LoginHandshakePacketHandler";
    }

    /**
     * Handles this packet
     * 
     * @param context The channel handler context to use
     * @param packet The packet
     * @since 1.0.0.1
     */
    @Override
    public void handle(final ChannelHandlerContext context, final Packet packet) {
        
        //Get the username
        String username = NettyStringHandler.readUTF16(packet.getBuffer());
        
        //Get the response string
        String connectionHash;
        
        //Check to see if we verify usernames
        if ((Boolean) Universe.getInstance().getSetting("verifyUsernames", true)) {
            //Add a new securely random long as a hexadecimal string
            connectionHash = Long.toHexString(Universe.getInstance().getSecureRandom().nextLong());
            //And add the unverified username
            Universe.getInstance().getUnverifiedUsernames().put(username, connectionHash);
        } else {
            //-
            connectionHash = "-";
        }        
        
        //Send the handshake response
        new LoginHandshakeResponsePacket(packet.getChannel()).sendHandshake(connectionHash);
        
    }
    
}
