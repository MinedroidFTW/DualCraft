package dualcraft.org.server.classic.net.packet;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for creating <code>Packet</code> objects.
 * 
 */
public final class PacketBuilder {
	
	/**
	 * The packet definition.
	 */
	private PacketDefinition definition;
	
	/**
	 * The values.
	 */
	private Map<String, Object> values = new HashMap<String, Object>();
	
	/**
	 * Creates the packet builder.
	 * @param definition The packet definition.
	 */
	public PacketBuilder(PacketDefinition definition) {
		this.definition = definition;
	}
	
	/**
	 * Creates a packet object based on this builder.
	 * @return The packet object.
	 */
	public Packet toPacket() {
		return new Packet(definition, values);
	}
	
	/**
	 * Validates that a field has been used correctly.
	 * @param name The field name.
	 * @param type The data type.
	 */
	private void validateField(String name, DataType type) {
		for (PacketField f : definition.getFields()) {
			if (f.getName().equals(name)) {
				if (f.getType().equals(type)) {
					return;
				} else {
					throw new IllegalArgumentException("Incorrect data type - expecting " + f.getType() + ".");
				}
			}
		}
		throw new IllegalArgumentException("No field named " + name + ".");
	}
	
	/**
	 * Puts a byte.
	 * @param name The name.
	 * @param value The value.
	 */
	public PacketBuilder putByte(String name, int value) {
		validateField(name, DataType.BYTE);
		values.put(name, (byte) value);
		return this;
	}
	
	/**
	 * Puts a short.
	 * @param name The name.
	 * @param value The value.
	 */
	public PacketBuilder putShort(String name, int value) {
		validateField(name, DataType.SHORT);
		values.put(name, (short) value);
		return this;
	}
	
	/**
	 * Puts an integer.
	 * @param name The name.
	 * @param value The value.
	 */
	public PacketBuilder putInt(String name, int value) {
		validateField(name, DataType.INT);
		values.put(name, value);
		return this;
	}
	
	/**
	 * Puts a long.
	 * @param name The name.
	 * @param value The value.
	 */
	public PacketBuilder putLong(String name, long value) {
		validateField(name, DataType.LONG);
		values.put(name, value);
		return this;
	}
	
	/**
	 * Puts a string.
	 * @param name The name.
	 * @param value The string.
	 */
	public PacketBuilder putString(String name, String value) {
		validateField(name, DataType.STRING);
		if (value.length() > 64) {
			throw new IllegalArgumentException("String exceeds maximum length of 64 characters.");
		}
		values.put(name, value);
		return this;
	}
	
	/**
	 * Puts a byte array.
	 * @param name The name.
	 * @param value The byte array.
	 */
	public PacketBuilder putByteArray(String name, byte[] value) {
		validateField(name, DataType.BYTE_ARRAY);
		if (value.length > 1024) {
			throw new IllegalArgumentException("Byte array exceeds maximum length of 1024 characters.");
		}
		values.put(name, value);
		return this;
	}
	
}
