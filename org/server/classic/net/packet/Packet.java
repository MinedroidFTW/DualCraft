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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single packet.
 * 
 */
public final class Packet {
	
	/**
	 * The packet definition.
	 */
	private final PacketDefinition definition;
	
	/**
	 * A map of field name to field data.
	 */
	private final Map<String, Object> fields;
	
	/**
	 * Creates the packet.
	 * @param definition The definition.
	 * @param fields The field map.
	 */
	public Packet(PacketDefinition definition, Map<String, Object> fields) {
		this.definition = definition;
		this.fields = Collections.unmodifiableMap(new HashMap<String, Object>(fields));
	}
	
	/**
	 * Gets the definition of this packet.
	 * @return The definition of this packet.
	 */
	public PacketDefinition getDefinition() {
		return definition;
	}
	
	/**
	 * Gets a numeric field.
	 * @param fieldName The name of the field.
	 * @return The value of the numeric field.
	 */
	public Number getNumericField(String fieldName) {
		return (Number) fields.get(fieldName);
	}
	
	/**
	 * Gets a string field.
	 * @param fieldName The name of the field.
	 * @return The value of the string field.
	 */
	public String getStringField(String fieldName) {
		return (String) fields.get(fieldName);
	}
	
	/**
	 * Gets a byte array field.
	 * @param fieldName The name of the field.
	 * @return The value of the byte array field.
	 */
	public byte[] getByteArrayField(String fieldName) {
		return (byte[]) fields.get(fieldName);
	}
}
