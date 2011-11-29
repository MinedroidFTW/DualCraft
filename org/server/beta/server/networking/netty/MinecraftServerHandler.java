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
package dualcraft.org.server.beta.server.networking.netty;

import dualcraft.org.server.beta.server.networking.packet.Packet;
import dualcraft.org.server.beta.server.networking.packet.PacketRegister;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * The server handler for Minecraft
 * 
 * 
 * @version 0.1.1.0
 */
public class MinecraftServerHandler extends SimpleChannelUpstreamHandler {
        
    /**
     * This handler's console logger
     * 
     * @since 0.1.0.0
     */
    protected final static Logger LOGGER = Logger.getLogger("MC Server Handler");
       
    /**
     * Called when an unhandled exception is caught
     * 
     * @param context The channel handler context
     * @param event The exception event
     * @since 0.1.0.1
     */
    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final ExceptionEvent event) {
        //Close the connection
        event.getChannel().close();
        
        //See if the player quit badly
        if (event.getCause().getMessage().contains("forcibly closed by the remote host")) {
            //TODO Save the player
            return;
        }
        
        //And log the exception
        LOGGER.log(Level.SEVERE, "Exception caught: ", event.getCause());
    }

    /**
     * Called when a message is received
     * 
     * @param context The channel handler context
     * @param event The message event
     * @since 0.1.0.0
     */
    @Override
    public void messageReceived(final ChannelHandlerContext context, final MessageEvent event) {
        //Get the buffer
        final ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
        //And set the packet
        final Packet packet = new Packet(event.getChannel(), buffer);
        
        //Execute via the packet register :)
        PacketRegister.getInstance().executeHandler(context, packet);
    }
    
    
    
}
