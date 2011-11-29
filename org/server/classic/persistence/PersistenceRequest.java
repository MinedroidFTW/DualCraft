package dualcraft.org.server.classic.persistence;

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
import org.slf4j.*;

import dualcraft.org.server.classic.model.Player;

/**
 * Represents a single persistence request.
 * 
 *
 */
public abstract class PersistenceRequest implements Runnable {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PersistenceRequest.class);
	
	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * Creates a persistence request for the specified player.
	 * @param player The player.
	 */
	public PersistenceRequest(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the player.
	 * @return The player.
	 */
	public final Player getPlayer() {
		return player;
	}
	
	/**
	 * Performs the persistence request.
	 */
	public abstract void perform() throws IOException;
	
	/**
	 * Calls the <code>perform</code> method to actually run the persistence
	 * request.
	 */

	public final void run() {
		try {
			perform();
			logger.info(getClass().getName() + " for : " + player.getName() + " succeeded.");
		} catch (IOException ex) {
			logger.error(getClass().getName() + " for : " + player.getName() + " failed.", ex);
			player.getSession().getActionSender().sendLoginFailure("Persistence request failed. Please try again.");
		}
	}
	
}
