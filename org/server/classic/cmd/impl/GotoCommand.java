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
import dualcraft.org.server.classic.Server;
import dualcraft.org.server.classic.security.Permission;

/**
 * A command to send a player to a new world
 * 
 */

public class GotoCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final GotoCommand INSTANCE = new GotoCommand ();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static GotoCommand getCommand() {
		return INSTANCE;
	}

	public String name() {
		return "goto";
	}
	
	/**
	 * Default private constructor.
	 */
	private GotoCommand () {
		/* empty */
	}
	
	public void execute(Player player, CommandParameters params) {
		if (player.isAuthorized(new Permission("dualcraft.org.server.classic.Worlds."+params.getStringArgument(0)+".goto"))) {
			if (Server.getServer().hasWorld(params.getStringArgument(0))) {
				player.getActionSender().sendChatMessage("Loading "+params.getStringArgument(0));
				player.moveToWorld(Server.getServer().getWorld(params.getStringArgument(0)));
			} else {
				player.getActionSender().sendChatMessage("World "+params.getStringArgument(0) + " is not loaded.");
			}
		} else {
			player.getActionSender().sendChatMessage("You are not permitted to go to "+params.getStringArgument(0));
		}
	}

}
