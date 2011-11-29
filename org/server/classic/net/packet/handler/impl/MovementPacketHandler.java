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

import dualcraft.org.server.classic.model.Player;
import dualcraft.org.server.classic.model.Position;
import dualcraft.org.server.classic.model.Rotation;
import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.net.packet.handler.PacketHandler;

/**
 * A packet handler which handles movement packets.
 * 
 */
public class MovementPacketHandler implements PacketHandler<MinecraftSession> {
	
	public void handlePacket(MinecraftSession session, Packet packet) {
		if (!session.isAuthenticated()) {
			return;
		}
		final int x = packet.getNumericField("x").intValue();
		final int y = packet.getNumericField("y").intValue();
		final int z = packet.getNumericField("z").intValue();
		final int rotation = packet.getNumericField("rotation").intValue();
		final int look = packet.getNumericField("look").intValue();
		final Player player = session.getPlayer();
		player.setPosition(new Position(x, y, z));
		player.setRotation(new Rotation(rotation, look));
	}
	
}
