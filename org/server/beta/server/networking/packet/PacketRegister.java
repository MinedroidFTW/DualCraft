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

import dualcraft.org.server.beta.server.networking.packet.handlers.UnimplementedPacketHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * A register of packet handlers
 * 
 * 
 * @version 1.0.0.2
 */
public class PacketRegister {
    
    /**
     * The instance of this class
     */
    private static PacketRegister instance;
    
    /**
     * Get the instance of this class
     * @return The instance
     */
    public static PacketRegister getInstance() {
        if (instance == null) {
            instance = new PacketRegister();
        }
        return instance;
    }
    
    /**
     * The map of packet handlers
     */
    private transient Map<Integer, AbstractPacketHandler> handlers = new HashMap<Integer, AbstractPacketHandler>(255);
    
    /**
     * Gets the map of handlers
     * @return The map of handlers
     */
    public Map<Integer, AbstractPacketHandler> getHandlers() {
        return this.handlers;
    }
    
    /**
     * Gets the handler for the packet requested
     * @param packetID The ID to search for
     * @return The packet handler
     */
    public AbstractPacketHandler getHandler(final int packetID) {
        //Is the packet handled?
        return this.handlers.containsKey(packetID) ? 
                //Yes, get the handler
                this.handlers.get(packetID) : 
                //No, get an ew packet handler
                new UnimplementedPacketHandler();
    }
    
    /**
     * Executes a packet handler
     * 
     * @param context The context to use
     * @param packet The packet to use
     */
    public void executeHandler(ChannelHandlerContext context, Packet packet) {
        this.getHandler(packet.getId()).handle(context, packet);
    }
    
    /**
     * Registers a new packet handler
     * @param packetID The ID to use
     * @param handler The handler to execute
     */
    public void registerHandler(int packetID, AbstractPacketHandler handler) {        
        this.handlers.put(packetID, handler);
        Logger.getLogger("PacketRegister").finer("Registered packet " + packetID + " to " + handler.getName());
    }
    
}
