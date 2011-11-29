package dualcraft.org.server.classic.net.packet.handler;

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

import java.util.Map;
import org.slf4j.*;

import dualcraft.org.server.classic.net.DCSession;
import dualcraft.org.server.classic.net.packet.Packet;

/**
 * A class which manages <code>PacketHandler</code>s.
 * 
 */
public class PacketHandlerManager<SessionType extends DCSession> {
	
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PacketHandlerManager.class);
	
	
	/**
	 * An array of packet handlers.
	 */
	private PacketHandler[] handlers = new PacketHandler[256];
	
	/**
	 * Default private constructor.
	 */
	@SuppressWarnings("unchecked")
	protected PacketHandlerManager(Map<Integer, String> map) {
		try {
			Map<Integer, String> handlers = map;
			for (Map.Entry<Integer, String> handler : handlers.entrySet()) {
				this.handlers[handler.getKey()] = (PacketHandler) Class.forName(handler.getValue()).newInstance();
			}
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	/**
	 * Handles a packet.
	 * @param session The session.
	 * @param packet The packet.
	 */
	public void handlePacket(SessionType session, Packet packet) {
		PacketHandler handler = handlers[packet.getDefinition().getOpcode()];
		if (handler != null) {
			handler.handlePacket(session, packet);
		} else {
			logger.warn("Unhandled packet : " + packet + ".");
		}
	}
	
}
