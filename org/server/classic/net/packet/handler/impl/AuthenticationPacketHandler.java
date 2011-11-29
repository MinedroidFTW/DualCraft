package dualcraft.org.server.classic.net.packet.handler.impl;

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

import org.slf4j.*;

import dualcraft.org.server.classic.Constants;
import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.net.packet.handler.PacketHandler;
import dualcraft.org.server.classic.Server;

/**
 * Handles the incoming authentication packet.
 * 
 */
public final class AuthenticationPacketHandler implements PacketHandler<MinecraftSession> {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationPacketHandler.class);
	
	public void handlePacket(MinecraftSession session, Packet packet) {
		if (session.isAuthenticated()) {
			return;
		}
		
		String username = packet.getStringField("username");
		String verificationKey = packet.getStringField("verification_key");
		int protocolVersion = packet.getNumericField("protocol_version").intValue();
		logger.info("Received authentication packet : username=" + username + ", verificationKey=" + verificationKey + ", protocolVersion=" + protocolVersion + ".");
		
		if (protocolVersion != Constants.PROTOCOL_VERSION) {
			session.getActionSender().sendLoginFailure("Incorrect protocol version.");
		} else {
			Server.getServer().register(session, username, verificationKey);
		}
	}
}
