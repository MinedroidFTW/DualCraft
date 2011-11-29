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
 * Holds the different Minecraft data types.
 * 
 */
public enum DataType {
	
	/**
	 * Standard byte data type.
	 */
	BYTE(1),

	/**
	 * Standard short data type.
	 */
	SHORT(2),

	/**
	 * Standard integer data type.
	 */
	INT(4),

	/**
	 * Standard long data type.
	 */
	LONG(8),

	/**
	 * Fixed-length (1024) byte array data type.
	 */
	BYTE_ARRAY(1024),

	/**
	 * Fixed length (64 ASCII bytes) string data type.
	 */
	STRING(64);
	
	/**
	 * The length of the data type, in bytes.
	 */
	private int length;
	
	/**
	 * Creates the data type.
	 * @param length
	 */
	private DataType(int length) {
		this.length = length;
	}
	
	/**
	 * Gets the length of this data type.
	 * @return The length, in bytes.
	 */
	public int getLength() {
		return length;
	}
	
}
