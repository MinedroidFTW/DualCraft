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
 * Represents a particular field in a packet.
 * 
 */
public final class PacketField {
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The type.
	 */
	private final DataType type;
	
	/**
	 * Creates the field.
	 * @param name The name of the field.
	 * @param type The field's data type.
	 */
	public PacketField(String name, DataType type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Gets the name of this field.
	 * @return The name of this field.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the data type of this field.
	 * @return The data type of this field.
	 */
	public DataType getType() {
		return type;
	}
	
}
