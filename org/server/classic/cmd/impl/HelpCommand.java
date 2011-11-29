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
 * Official /help command
 * 
 */

public class HelpCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final HelpCommand INSTANCE = new HelpCommand();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static HelpCommand getCommand() {
		return INSTANCE;
	}

	public String name() {
		return "help";
	}
	
	/**
	 * Default private constructor.
	 */
	private HelpCommand() {
		/* empty */
	}
	
	public void execute(Player player, CommandParameters params) {
		String message = player.getWorld().getGameMode().listCommands();
		while (message.length() > 0) {
			// this is a short list so send it and leave
			if (message.length() < 64) {
				player.getActionSender().sendChatMessage(message);
				return;
			}

			int end = 64;
			while (end > 0) {
				// Look for a space in which to nicely break up the list
				// does NOT cover commands that are greater than 64 characters in length
				if (message.charAt(end) == ' ') {
					// chop the string up and send the first chunk
					player.getActionSender().sendChatMessage(message.substring(0, end));
					message = message.substring(end+1, message.length());
					break;
				}
				end -= 1;
			}
		}
	}

}
