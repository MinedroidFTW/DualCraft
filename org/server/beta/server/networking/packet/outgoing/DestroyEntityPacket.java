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

import dualcraft.org.server.beta.server.model.entity.AbstractEntity;
import dualcraft.org.server.beta.server.networking.packet.OutgoingPacket;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

/**
 * An outgoing packet that destroys an entity on the client
 * 
 * 
 * @version 1.0.0.0
 */
public class DestroyEntityPacket extends OutgoingPacket {
    
    /**
     * Creates a new destroy entity packet
     * 
     * @param channel The channel to send to
     * @since 0.9.0.0
     */
    public DestroyEntityPacket(Channel channel) {
        super(channel);
    }
    
    /**
     * Destroys an entity on the client with the specified ID
     * 
     * @param entityID The ID of the entity to destroy
     * @return The channel's future
     * @since 0.9.0.0
     */
    public ChannelFuture destroy(int entityID) {
        //Write the ID
        this.getBuffer().writeByte(28);
        
        //And write the entity ID
        this.getBuffer().writeInt(entityID);
        
        //And write
        return this.writePacket();        
    }
    
    /**
     * Destroys an entity on the client with the specified ID
     * 
     * @param entity The entity to destroy
     * @return The channel's future
     * @since 1.0.0.0
     */
    public ChannelFuture destroy(AbstractEntity entity) {
        //Write the ID
        this.getBuffer().writeByte(28);
        
        //And write the entity ID
        this.getBuffer().writeInt(entity.getEntityID());
        
        //And write
        return this.writePacket();        
    }
    
}