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
package dualcraft.org.server.classic.net;

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.mina.core.session.IoSession;
import dualcraft.org.server.classic.net.packet.Packet;

import org.slf4j.*;

/**
 * 
 * Represents an object that is bound to either end of a socket.
 */
public abstract class Connectable {
	
	/**
	 * Packet queue.
	 */
	protected final Queue<Packet> queuedPackets = new ArrayDeque<Packet>();

	private static final Logger logger = LoggerFactory.getLogger(Connectable.class);
	
	/**
	 * State.
	 */
	protected State state = State.CONNECTED;
	
	/**
	 * Sends a packet. This method may be called from multiple threads.
	 * @param packet The packet to send.
	 */
	protected void send(Packet packet, IoSession session) {
		synchronized (this) {
			final String name = packet.getDefinition().getName();
			final boolean unqueuedPacket = name.equals("authentication_response") || name.endsWith("level_init") || name.equals("level_block") || name.equals("level_finish") || name.equals("disconnect");
			if (state == State.READY) {
				if (queuedPackets.size() > 0) {
					for (Packet queuedPacket : queuedPackets) {
						session.write(queuedPacket);
					}
					queuedPackets.clear();
				}
				session.write(packet);
			} else if (unqueuedPacket) {
				session.write(packet);
			} else {
				queuedPackets.add(packet);
			}
		}
	}
	
	
}
