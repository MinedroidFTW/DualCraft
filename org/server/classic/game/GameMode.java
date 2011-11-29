package dualcraft.org.server.classic.game;

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

import java.util.Map;

import dualcraft.org.server.classic.cmd.Command;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Player;

/**
 * An interface which represents a specific type of game mode.
 * 
 * 
 */
public interface GameMode<P extends Player> {
	
	/**
	 * Gets a map of commands that are supported in this game mode.
	 * @return The map of commands.
	 */
	public Map<String, Command> getCommands();

	 /**
	  * Lists all the commands for use by a command like /help
	  */
	public String listCommands();
	
	/**
	 * Called every 100ms BEFORE each tick.
	 */
	public void tick();
	
	/**
	 * Notification of player connected
	 * @param player The connected player
	 */
	public void playerConnected(P player);
	
	/**
	 * Event handler for a player disconnect Remember player has already
	 * disconnected!
	 * @param player The disconnected player
	 */
	public void playerDisconnected(P player);
	
	/**
	 * Handles block adding and removing
	 * @param player The player setting the block
	 * @param level The level
	 * @param mode 1/0 adding/removing
	 * @param type typeId of the block
	 */
	public void setBlock(P player, Level level, int x, int y, int z, int mode, int type);
	
	/**
	 * Broadcasts a chat message.
	 * @param player The sending player.
	 * @param message The chat message.
	 */
	public void broadcastChatMessage(P player, String message);
}
