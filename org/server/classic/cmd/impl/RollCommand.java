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
import java.util.Random;

/**
 * A command to simulate dice rolls
 * 
 */

public class RollCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final RollCommand INSTANCE = new RollCommand();

	public String name() {
		return "roll";
	}
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static RollCommand getCommand() {
		return INSTANCE;
	}
	
	/**
	 * Default private constructor.
	 */
	private RollCommand() {
		/* empty */
	}
	
	public void execute(Player player, CommandParameters params) {
		if (params.getArgumentCount() != 1) {
			player.getActionSender().sendChatMessage("<dice>d<sides>");
			player.getActionSender().sendChatMessage("Roll a dice with <sides> number of sides");
			player.getActionSender().sendChatMessage("and <dice> number of times");
			player.getActionSender().sendChatMessage("<dice> will be no greateer than 9.");
			player.getActionSender().sendChatMessage("<sides> will be no greater than 99.");
			player.getActionSender().sendChatMessage("Ex. 2d6 will roll a six-sided die two times");
			player.getActionSender().sendChatMessage("/roll <XdYY>");
			return;
		}

		String message = "";
		String arg[] = params.getStringArgument(0).split("d");
		if (arg.length != 2) {
			player.getActionSender().sendChatMessage("/roll <XdYY>");
			return;
		}
		try {
			int dice  = Math.abs(Integer.parseInt(arg[0]));
			dice = dice > 9 ? 9 : dice;
			int sides = Math.abs(Integer.parseInt(arg[1]));
			sides = sides > 99 ? 99 : sides;
			Random r = new Random();
			while (dice > 0) {
				dice -= 1;
				message += r.nextInt(sides) + ", ";
			}
			// clean up the extra ", " if it exists
			if (message.length() > 2) {
				message = message.substring(0, message.length() - 2);
			}
		} catch (Exception e) {
			player.getActionSender().sendChatMessage("Please input valid numbers");
		}
		player.getActionSender().sendChatMessage(message);
	}
	
}
