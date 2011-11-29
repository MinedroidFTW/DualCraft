package dualcraft.org.server.classic.cmd.impl;

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

import dualcraft.org.server.classic.cmd.Command;
import dualcraft.org.server.classic.cmd.CommandParameters;
import dualcraft.org.server.classic.model.Player;

/**
 * Official /summon command
 * 
 */

public class SpawnCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final SpawnCommand INSTANCE = new SpawnCommand();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static SpawnCommand getCommand() {
		return INSTANCE;
	}

	public String name() {
		return "spawn";
	}

	/**
	 * Default private constructor.
	 */
	private SpawnCommand() {
		/* empty */
	}
	
	public void execute(Player player, CommandParameters params) {
		player.teleport(player.getWorld().getLevel().getSpawnPosition(), player.getWorld().getLevel().getSpawnRotation());
	}

}
