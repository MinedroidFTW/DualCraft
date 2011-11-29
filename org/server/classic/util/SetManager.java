package dualcraft.org.server.classic.util;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages sets of users such as operators and banned users.
 * 
 *
 */
public class SetManager {
	
	/**
	 * The singleton instance.
	 */
	private static SetManager INSTANCE = new SetManager();
	
	/**
	 * Gets the set manager instance.
	 * @return The set manager instance.
	 */
	public static SetManager getSetManager() {
		return INSTANCE;
	}
	
	/**
	 * Default private constructor.
	 */
	private SetManager() {
		/* empty */
	}
	
	/**
	 * A map of sets.
	 */
	private Map<String, Set<String>> sets = new HashMap<String, Set<String>>();
	
	/**
	 * Gets a set.
	 * @param name The name of the set.
	 * @return The set (read only).
	 * @throws IllegalArgumentException if the set does not exist.
	 */
	public Set<String> getSet(String name) {
		if(!sets.containsKey(name)) {
			throw new IllegalArgumentException("Set does not exist.");
		}
		return Collections.unmodifiableSet(sets.get(name));
	}
	
	/**
	 * Adds the name of a player to the specified set.
	 * @param name The name of the set.
	 * @param player The name of the player.
	 */
	public void addToSet(String name, String player) {
		if(sets.containsKey(name)) {
			sets.get(name).add(player);
		}
	}
	
	/**
	 * Removes the name of a player from the specified set.
	 * @param name The name of the set.
	 * @param player The name of the player.
	 */
	public void removeFromSet(String name, String player) {
		if(sets.containsKey(name)) {
			sets.get(name).remove(player);
		}
	}
	
	/**
	 * Removes all player's names from the specified set.
	 * @param name The name of the set.
	 */
	public void removeAllFromSet(String name) {
		if(sets.containsKey(name)) {
			sets.get(name).clear();
		}
	}
	
	/**
	 * Reloads sets from the file system.
	 * @throws IOException if an I/O error occurs.
	 */
	public void reloadSets() throws IOException {
		reloadSet("ops");
		reloadSet("banned");
	}

	/**
	 * Reloads the specified set from the file system.
	 * @param name The set.
	 * @throws IOException if an I/O error occurs.
	 */
	private void reloadSet(String name) throws IOException {
		Set<String> set = sets.get(name);
		if(set == null) {
			set = new HashSet<String>();
			sets.put(name, set);
		}
		File f = new File("./data/" + name + ".txt");
		if(!f.exists()) {
			f.createNewFile();
		}
		BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		try {
			String line;
			while((line = rdr.readLine()) != null) {
				set.add(line.trim());
			}
		} finally {
			rdr.close();
		}
	}
	
}
