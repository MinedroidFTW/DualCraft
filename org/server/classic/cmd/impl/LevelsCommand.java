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
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Environment;
import dualcraft.org.server.classic.model.Builder;
import dualcraft.org.server.classic.Server;
import dualcraft.org.server.classic.security.Permission;
import dualcraft.org.server.classic.model.impl.builders.*;

/**
 * A command that generates a new world
 * 
 */

public class LevelsCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final LevelsCommand INSTANCE = new LevelsCommand();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static LevelsCommand getCommand() {
		return INSTANCE;
	}

	public String name() {
		return "levels";
	}
	
	/**
	 * Default private constructor.
	 */
	private LevelsCommand () {
		/* empty */
	}
	
	public void execute(Player player, CommandParameters params) {
		String[] names = Server.getServer().getLoadedWorldNames();
		player.getActionSender().sendChatMessage("Loaded Worlds:");
		String message = "" + (char)(0xf);
		for (int i = 0; i < names.length; i++) {
			if (message.length() == 1) {
				message += "&a" + names[i];
			} else {
				// 6 is the magic number for "&e, &a"
				if (message.length() + 6 + names[i].length() < 64) {
					message += "&e, &a" + names[i];
				} else {
					player.getActionSender().sendChatMessage(message);
					message = " ";
					i -= 1;
				}
			}
		}

		if (message.length() > 0) {
			player.getActionSender().sendChatMessage(message);
		}

		player.getActionSender().sendChatMessage("Unloaded Worlds:");
		player.getActionSender().sendChatMessage("-Not implemented yet-");
	}

}
