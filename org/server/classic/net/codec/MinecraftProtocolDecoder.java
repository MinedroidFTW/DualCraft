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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.net.packet.PacketDefinition;
import dualcraft.org.server.classic.net.packet.PacketField;
import dualcraft.org.server.classic.net.packet.PacketManager;

/**
 * An implement of a <code>ProtocolDecoder</code> which decodes buffers into
 * Minecraft packet objects then dispatches them.
 * 
 */
public final class MinecraftProtocolDecoder extends CumulativeProtocolDecoder {
	
	/**
	 * The current packet being decoded.
	 */
	private PacketDefinition currentPacket = null;
	private PacketManager manager;
	
	public MinecraftProtocolDecoder(PacketManager manager)
	{
		this.manager = manager;
	}
	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput out) throws Exception {
		if (currentPacket == null) {
			if (buffer.remaining() >= 1) {
				int opcode = buffer.getUnsigned();
				currentPacket = manager.getIncomingPacket(opcode);
				if (currentPacket == null) {
					throw new IOException("Unknown incoming packet type (opcode = " + opcode + ").");
				}
			} else {
				return false;
			}
		}
		if (buffer.remaining() >= currentPacket.getLength()) {
			Map<String, Object> values = new HashMap<String, Object>();
			for (PacketField field : currentPacket.getFields()) {
				Object value = null;
				switch (field.getType()) {
				case BYTE:
					value = buffer.get();
					break;
				case SHORT:
					value = buffer.getShort();
					break;
				case INT:
					value = buffer.getInt();
					break;
				case LONG:
					value = buffer.getLong();
					break;
				case BYTE_ARRAY:
					value = IoBuffer.allocate(1024).put(buffer);
					break;
				case STRING:
					byte[] bytes = new byte[64];
					buffer.get(bytes);
					value = new String(bytes).trim();
					break;
				}
				values.put(field.getName(), value);
			}
			Packet packet = new Packet(currentPacket, values);
			currentPacket = null;
			out.write(packet);
			return true;
		} else {
			return false;
		}
	}
	
}
