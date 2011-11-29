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

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import dualcraft.org.server.classic.net.codec.MinecraftCodecFactory;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.task.TaskQueue;
import dualcraft.org.server.classic.task.impl.SessionClosedTask;
import dualcraft.org.server.classic.task.impl.SessionMessageTask;
import dualcraft.org.server.classic.task.impl.SessionOpenedTask;
import org.slf4j.*;

/**
 * An implementation of an <code>IoHandler</code> which manages incoming events
 * from MINA and passes them onto the necessary subsystem in the DualCraft
 * server.
 * 
 */
public final class SessionHandler extends IoHandlerAdapter {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);
	
	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		logger.error("Exception occurred, closing session.", throwable);
		session.close(false);
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		TaskQueue.getTaskQueue().push(new SessionMessageTask(session, (Packet) message));
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.trace("Session closed. Queuing for cleanup.");
		TaskQueue.getTaskQueue().push(new SessionClosedTask(session));
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.trace("Session opened. Queuing for initialization.");
		session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(new MinecraftCodecFactory(PersistingPacketManager.getPacketManager())));
		TaskQueue.getTaskQueue().push(new SessionOpenedTask(session));
	}
	
}
