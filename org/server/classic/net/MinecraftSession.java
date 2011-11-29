package dualcraft.org.server.classic.net;

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

import org.apache.mina.core.session.IoSession;
import dualcraft.org.server.classic.model.Player;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.Server;
import org.slf4j.*;

/**
 * Manages a connected Minecraft session.
 * 
 */
public final class MinecraftSession extends DCSession{
	
	
	/**
	 * The action sender associated with this session.
	 */
	private final ActionSender actionSender = new ActionSender(this);
	
	private final static Logger logger = LoggerFactory.getLogger(MinecraftSession.class);
	
	/**
	 * The player associated with this session.
	 */
	private Player player;
	
	
	public MinecraftSession(IoSession sess) {
		super(sess);
	}
	/**
	 * Gets the action sender associated with this session.
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return actionSender;
	}
	
	/**
	 * Sets the player associated with this session.
	 * @param player The player.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the player associated with this session.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	
	/**
	 * Handles a packet.
	 * @param packet The packet to handle.
	 */
	@Override
	public void handle(Packet packet) {
		PersistingHandlerManager.getPacketHandlerManager().handlePacket(this, packet);
	}

	
	/**
	 * Called when this session is to be destroyed, should release any
	 * resources.
	 */
	@Override
	public void destroy() {
		logger.debug("Destroying session.");
		Server.getServer().unregister(this);
	}
	
	
	public boolean isAuthenticated() {
		if (player == null) {
			return false;
		} else {
			return true;
		}
	}
}
