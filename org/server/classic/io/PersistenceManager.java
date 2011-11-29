package dualcraft.org.server.classic.io;

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import dualcraft.org.server.classic.model.BlockDefinition;
import dualcraft.org.server.classic.model.BlockManager;
import dualcraft.org.server.classic.net.packet.PacketDefinition;
import dualcraft.org.server.classic.net.packet.PacketField;
import dualcraft.org.server.classic.net.packet.PacketManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.slf4j.*;

/**
 * A class which manages XStream persistence.
 * 
 */
public final class PersistenceManager {
	
	/**
	 * The singleton instance.
	 */
	private static final PersistenceManager INSTANCE = new PersistenceManager();

	private static final Logger logger = LoggerFactory.getLogger(PersistenceManager.class);
	
	/**
	 * Gets the persistence manager instance.
	 * @return The persistence manager instance.
	 */
	public static PersistenceManager getPersistenceManager() {
		return INSTANCE;
	}
	
	/**
	 * The XStream object.
	 */
	private final XStream xstream = new XStream(new DomDriver());
	
	/**
	 * Initializes the persistence manager.
	 */
	private PersistenceManager() {
		xstream.alias("packets", PacketManager.class);
		xstream.alias("packet", PacketDefinition.class);
		xstream.alias("field", PacketField.class);
		xstream.alias("blocks", BlockManager.class);
		xstream.alias("block", BlockDefinition.class);
	}
	
	/**
	 * Loads an object from an XML file.
	 * @param file The file.
	 * @return The object.
	 */
	public Object load(String file) {
		logger.trace("Loading from {}", file);
		Object ret;
		try {
			ret = xstream.fromXML(new FileInputStream(file));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
		logger.trace("Loaded!");
		return ret;
	}
	
	/**
	 * Saves an object to an XML file.
	 * @param file The file.
	 * @param o The object.
	 */
	public void save(String file, Object o) {
		try {
			xstream.toXML(o, new FileOutputStream(file));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Gets the xstream object.
	 * @return the xstream object.
	 */
	public XStream getXStream() {
		return xstream;
	}
	
}
