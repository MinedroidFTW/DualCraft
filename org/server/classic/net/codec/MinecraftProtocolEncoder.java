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

import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import dualcraft.org.server.classic.net.packet.Packet;
import dualcraft.org.server.classic.net.packet.PacketDefinition;
import dualcraft.org.server.classic.net.packet.PacketField;

/**
 * An implementation of a <code>ProtocolEncoder</code> which encodes Minecraft
 * packet objects into buffers and then dispatches them.
 * 
 */
public final class MinecraftProtocolEncoder extends ProtocolEncoderAdapter {
	
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Packet packet = (Packet) message;
		PacketDefinition def = packet.getDefinition();
		IoBuffer buf = IoBuffer.allocate(def.getLength() + 1);
		buf.put((byte) def.getOpcode());
		for (PacketField field : def.getFields()) {
			switch (field.getType()) {
			case BYTE:
				buf.put(packet.getNumericField(field.getName()).byteValue());
				break;
			case SHORT:
				buf.putShort(packet.getNumericField(field.getName()).shortValue());
				break;
			case INT:
				buf.putInt(packet.getNumericField(field.getName()).intValue());
				break;
			case LONG:
				buf.putLong(packet.getNumericField(field.getName()).longValue());
				break;
			case BYTE_ARRAY:
				byte[] data = packet.getByteArrayField(field.getName());
				byte[] resized = Arrays.copyOf(data, 1024);
				buf.put(resized);
				break;
			case STRING:
				String str = packet.getStringField(field.getName());
				data = str.getBytes();
				resized = Arrays.copyOf(data, 64);
				for (int i = 0; i < resized.length; i++) {
					if (resized[i] == 0) {
						resized[i] = ' ';
					}
				}
				buf.put(resized);
				break;
			}
		}
		buf.flip();
		out.write(buf);
	}
	
}
