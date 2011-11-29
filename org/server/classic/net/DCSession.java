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
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.model.Player;
import java.net.SocketAddress;

/**
 * 
 * The base class for all sessions .
 */
public abstract class DCSession extends Connectable{
	

	
	/**
	 * The <code>IoSession</code> associated with this
	 * <code>MinecraftSession</code>.
	 */
	protected final IoSession session;

	/**
	 * Creates the Minecraft session.
	 * @param session The <code>IoSession</code>.
	 */
	public DCSession(IoSession session) {
		this.session = session;
	}
	
	public String toString() {
		if (m_player == null)
			return "<none>@"+session.getRemoteAddress();
		return m_player.getName()+"@"+session.getRemoteAddress();
	}

	public SocketAddress getAddress() {
		return session.getRemoteAddress();
	}

	
	/**
	 * Sets the state to authenticated.
	 */
	public void setAuthenticated() {
		this.state = State.AUTHENTICATED;
	}
	
	/**
	 * Sets the state to ready.
	 */
	public void setReady() {
		this.state = State.READY;
	}
	
	/**
	 * Sends a packet. This method may be called from multiple threads.
	 * @param packet The packet to send.
	 */
	public void send(Packet packet) {
			this.send(packet, session);
	}
	
	/**
	 * Closes this session.
	 */
	public void close() {
		session.close(false);
	}
	
	/**
	 * Called when this session is to be destroyed, should release any
	 * resources.
	 */
	public abstract void destroy();
	
	private Player m_player;

	public void setPlayer(Player p) {
		m_player = p;
	}
	
	public Player getPlayer() {
		return m_player;
	}

	
	/**
	 * Handles a packet.
	 * @param packet The packet to handle.
	 */
	public abstract void handle(Packet packet);
}
