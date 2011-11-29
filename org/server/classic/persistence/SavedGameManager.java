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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dualcraft.org.server.classic.io.PersistenceManager;
import dualcraft.org.server.classic.model.Player;

import com.thoughtworks.xstream.XStream;

/**
 * The core class of the saved game system.
 * 
 *
 */
public class SavedGameManager {
	
	/**
	 * The singleton instance of the persistence manager.
	 */
	private static final SavedGameManager INSTANCE = new SavedGameManager();
	
	/**
	 * Gets the saved game manager instance.
	 * @return The saved game manager instance.
	 */
	public static SavedGameManager getSavedGameManager() {
		return INSTANCE;
	}
	
	/**
	 * The executor service in which persistence requests are executed.
	 */
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	/**
	 * Creates the saved game manager.
	 */
	private SavedGameManager() {
		/* empty */
	}
	
	/**
	 * Gets the xstream instance.
	 * @return The xstream instance.
	 */
	public XStream getXStream() {
		return PersistenceManager.getPersistenceManager().getXStream();
	}
	
	/**
	 * Gets the path to a saved game.
	 * @param player The player.
	 * @return The path to their saved game.
	 */
	public String getPath(Player player) {
		return "./data/savedGames/" + player.getName().toLowerCase() + ".xml";
	}
	
	/**
	 * Queues a persistence request.
	 * @param req The request to queue.
	 * @return The future object.
	 */
	public Future<?> queuePersistenceRequest(PersistenceRequest req) {
		return service.submit(req);
	}
	
}
