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

import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.net.packet.handler.PacketHandler;

/**
 * A packet handler which handles the construction packet.
 * 
 */
public class ConstructionPacketHandler implements PacketHandler<MinecraftSession> {
	
	public void handlePacket(MinecraftSession session, Packet packet) {
		if (!session.isAuthenticated()) {
			return;
		}
		int x = packet.getNumericField("x").intValue();
		int y = packet.getNumericField("y").intValue();
		int z = packet.getNumericField("z").intValue();
		int mode = packet.getNumericField("mode").intValue();
		int type = packet.getNumericField("type").intValue();
		session.getPlayer().getWorld().getGameMode().setBlock(session.getPlayer(), session.getPlayer().getWorld().getLevel(), x, y, z, mode, type);
	}
	
}
