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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dualcraft.org.server.classic.model.Player;

/**
 * A class which manages the list of connected players.
 * 
 */
public class PlayerList {
	
	/**
	 * The maximum number of players.
	 */
	//private final int maximumPlayers = Configuration.getConfiguration().getMaximumPlayers();
	//FIXME
	private final int maximumPlayers = 64;
	
	/**
	 * The player array.
	 */
	private final Player[] players = new Player[maximumPlayers];
	
	/**
	 * The size of the player array.
	 */
	private int size = 0;
	
	/**
	 * Default public constructor.
	 */
	public PlayerList() {
		/* empty */
	}
	
	/**
	 * Gets a list of online players.
	 * @return A list of online players.
	 */
	public Collection<Player> getPlayers() {
		List<Player> playerList = new LinkedList<Player>();
		for (Player p : players) {
			if (p != null) {
				playerList.add(p);
			}
		}
		return Collections.unmodifiableCollection(playerList);
	}
	
	/**
	 * Adds a player.
	 * @param player The new player.
	 * @return <code>true</code> if they could be added, <code>false</code> if
	 * not.
	 */
	public boolean add(Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				player.setId(i);
				size++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes a player.
	 * @param player The player to remove.
	 */
	public void remove(Player player) {
		int id = player.getId();
		if (id != -1 && players[id] == player) {
			players[id] = null;
			size--;
		}
		player.setId(-1);
	}
	
	/**
	 * Gets the number of online players.
	 * @return The player list size.
	 */
	public int size() {
		return size;
	}
	
}
