package dualcraft.org.server.classic.cluster;

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

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import dualcraft.org.server.classic.net.Connectable;
import dualcraft.org.server.classic.net.State;
import dualcraft.org.server.classic.net.codec.MinecraftCodecFactory;
import org.slf4j.*;

/**
 * 
 * A client that is connecting to a server in the cluster.
 */
public abstract class DCClient extends Connectable{

	/**
	 * The task's logging system.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DCClient.class);

	
	private static final int AWAIT_TIME = 3000;
	private final String IP;
	private final int PORT;

	
	/**
	 * What connects to the info server.
	 */
	private SocketConnector connector;


	/**
	 * The protocol factory for the client.
	 */
	private final MinecraftCodecFactory factory;
	
	public DCClient(String ip, int port, MinecraftCodecFactory factory)
	{
		this.IP = ip;
		this.PORT = port;
		this.factory = factory;
		this.connector = new NioSocketConnector();
	}
	/**
	 * Attempt to connect to the info server.
	 */
	public void connect()
	{
		try
		{
			ConnectFuture future = connector.connect(new InetSocketAddress(IP, PORT));
			future.awaitUninterruptibly(AWAIT_TIME);

			IoSession session = future.getSession();
			session.getFilterChain().addFirst("protocol", new ProtocolCodecFilter(factory));
			state = State.READY;
			this.onConnect(session);
		}
		catch(RuntimeIoException e)
		{
			this.onFailure(e);
		}
	}

	/**
	 * Requires what to do when it connects or fails to connect.
	 */
	public abstract void onConnect(IoSession session);
	public abstract void onFailure(Throwable e);
}
