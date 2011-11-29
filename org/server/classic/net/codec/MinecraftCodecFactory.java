package dualcraft.org.server.classic.net.codec;

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
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import dualcraft.org.server.classic.net.packet.PacketManager;

/**
 * A <code>ProtocolCodecFactory</code> for the Minecraft protocol.
 * 
 */
public final class MinecraftCodecFactory implements ProtocolCodecFactory {
	
	/**
	 * The decoder instance.
	 */
	private final ProtocolDecoder decoder;
	
	/**
	 * The encoder instance.
	 */
	private final ProtocolEncoder encoder;
	
	
	public MinecraftCodecFactory(PacketManager manager)
	{
		decoder = new MinecraftProtocolDecoder(manager);
		encoder = new MinecraftProtocolEncoder();
	}
	
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}
	
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
	
}
