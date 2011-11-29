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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import dualcraft.org.server.classic.model.Player;

import com.thoughtworks.xstream.XStream;

/**
 * A persistence request which loads the specified player.
 * 
 *
 */
public class LoadPersistenceRequest extends PersistenceRequest {
	
	/**
	 * Creates the load request.
	 * @param player The player to load.
	 */
	public LoadPersistenceRequest(Player player) {
		super(player);
	}

	@SuppressWarnings("unchecked")
	public void perform() throws IOException {
		final SavedGameManager mgr = SavedGameManager.getSavedGameManager();
		final Player player = getPlayer();
		final XStream xs = mgr.getXStream();
		final File file = new File(mgr.getPath(player));
		if(file.exists()) {
			try {
				Map<String, Object> attributes = (Map<String, Object>) xs.fromXML(new FileInputStream(file));
				for(Map.Entry<String, Object> entry : attributes.entrySet()) {
					player.setAttribute(entry.getKey(), entry.getValue());
				}
			} catch (RuntimeException ex) {
				throw new IOException(ex.getMessage());
			}
		}
		player.getSession().setReady();
		player.getWorld().completeRegistration(player.getSession());
	} 
}
