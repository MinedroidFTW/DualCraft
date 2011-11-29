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

import java.util.LinkedList;
import java.util.List;

/**
 * A utility class for managing the whole packet system.
 * @param Graham Edgecombe
 */
public class PacketManager {
	
	
	/**
	 * Incoming packets.
	 */
	private List<PacketDefinition> incoming = new LinkedList<PacketDefinition>();
	
	/**
	 * Outgoing packets.
	 */
	private List<PacketDefinition> outgoing = new LinkedList<PacketDefinition>();
	
	/**
	 * The incoming packet array (faster access by opcode than list iteration).
	 */
	private transient PacketDefinition[] incomingArray;
	
	/**
	 * The outgoing packet array (faster access by opcode than list iteration).
	 */
	private transient PacketDefinition[] outgoingArray;
	
	/**
	 * Default private constructor.
	 */
	protected PacketManager() {
		/* empty */
	}
	
	/**
	 * Resolves the packet manager after deserialization.
	 * @return The resolved object.
	 */
	private Object readResolve() {
		incomingArray = new PacketDefinition[256];
		for (PacketDefinition def : incoming) {
			incomingArray[def.getOpcode()] = def;
		}
		outgoingArray = new PacketDefinition[256];
		for (PacketDefinition def : outgoing) {
			outgoingArray[def.getOpcode()] = def;
		}
		return this;
	}
	
	/**
	 * Gets an incoming packet definition.
	 * @param opcode The opcode.
	 * @return The packet definition.
	 */
	public PacketDefinition getIncomingPacket(int opcode) {
		return incomingArray[opcode];
	}
	
	/**
	 * Gets an outgoing packet definition.
	 * @param opcode The opcode.
	 * @return The packet definition.
	 */
	public PacketDefinition getOutgoingPacket(int opcode) {
		return outgoingArray[opcode];
	}
	
}
