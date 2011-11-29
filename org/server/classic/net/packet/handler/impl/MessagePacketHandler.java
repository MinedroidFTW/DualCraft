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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dualcraft.org.server.classic.cmd.Command;
import dualcraft.org.server.classic.cmd.CommandParameters;
import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.net.packet.handler.PacketHandler;
import dualcraft.org.server.classic.security.Permission;

/**
 * A class which handles message and comamnd packets.
 * 
 */
public class MessagePacketHandler implements PacketHandler<MinecraftSession> {
	
	public void handlePacket(MinecraftSession session, Packet packet) {
		if (!session.isAuthenticated()) {
			return;
		}
		String message = packet.getStringField("message");
		if (message.startsWith("/")) {
			// interpret as command
			String tokens = message.substring(1);
			String[] parts = tokens.split(" ");
			final Map<String, Command> commands = session.getPlayer().getWorld().getGameMode().getCommands();
			Command c = commands.get(parts[0]);
			if (session.getPlayer().isAuthorized(new Permission(c.getClass().getName()+".execute"))) {
				if (c != null) {
					parts[0] = null;
					List<String> partsList = new ArrayList<String>();
					for (String s : parts) {
						if (s != null) {
							partsList.add(s);
						}
					}
					parts = partsList.toArray(new String[0]);
					c.execute(session.getPlayer(), new CommandParameters(parts));
				} else {
					session.getActionSender().sendChatMessage("Invalid command.");
				}
			} else {
				session.getActionSender().sendChatMessage("You are not permitted to execute the "+parts[0]+" command.");
			}
		} else {
			session.getPlayer().getWorld().getGameMode().broadcastChatMessage(session.getPlayer(), message);
		}
	}
	
}
