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
/**
 * Represents a type of packet.
 * 
 */
public final class PacketDefinition {
	
	/**
	 * The opcode of this packet.
	 */
	private final int opcode;
	
	/**
	 * The name of this packet.
	 */
	private final String name;
	
	/**
	 * The fields in this packet.
	 */
	private final PacketField[] fields;
	
	/**
	 * The length of this packet.
	 */
	private final transient int length;
	
	/**
	 * Creates the packet definition.
	 * @param opcode The opcode.
	 * @param name The name.
	 * @param fields The fields.
	 */
	public PacketDefinition(int opcode, String name, PacketField[] fields) {
		this.opcode = opcode;
		this.name = name;
		this.fields = fields;
		// compute packet length
		int length = 0;
		for (PacketField field : fields) {
			length += field.getType().getLength();
		}
		this.length = length;
	}
	
	/**
	 * Resolves this object.
	 * @return The object with the correct packet length.
	 */
	private Object readResolve() {
		// ensures length is computed
		return new PacketDefinition(opcode, name, fields);
	}
	
	/**
	 * Gets the opcode of this packet.
	 * @return The opcode of this packet.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Gets the name of this packet.
	 * @return The name of this packet.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the fields in this packet.
	 * @return The fields in this packet.
	 */
	public PacketField[] getFields() {
		return fields;
	}
	
	/**
	 * Gets the length of this packet.
	 * @return The length of this packet, in bytes.
	 */
	public int getLength() {
		return length;
	}
	
}
