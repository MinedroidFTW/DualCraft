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
import dualcraft.org.server.classic.io.LevelManager;

/**
 * A command that generates a new world
 * 
 */

public class GenerateCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final GenerateCommand INSTANCE = new GenerateCommand();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static GenerateCommand getCommand() {
		return INSTANCE;
	}

	public String name() {
		return "generate";
	}
	
	/**
	 * Default private constructor.
	 */
	private GenerateCommand () {
		/* empty */
	}
	
	public void execute(Player player, CommandParameters params) {
			String theme = "Summer";
			if (params.getArgumentCount() > 6 || params.getArgumentCount() < 5) {
				player.getActionSender().sendChatMessage("<name> - The name of the map");
				player.getActionSender().sendChatMessage("<x> <y> <z> - The width, height, and depth of the level");
				player.getActionSender().sendChatMessage("<type> - The type of level to generate");
				player.getActionSender().sendChatMessage("Valid types are 'Hills' 'Flat' 'Pixel'");
				player.getActionSender().sendChatMessage("<theme> - The tile set to use");
				player.getActionSender().sendChatMessage("Valid themes are 'Summer' 'Winter' 'Oasis'");
				player.getActionSender().sendChatMessage("/generate <name> <x> <y> <z> <type> [<theme>]");
				return;
			}


			try {
				String name = params.getStringArgument(0);
				if (Server.getServer().hasWorld(name)) {
					player.getActionSender().sendChatMessage("World is loaded. Stopping");
					return;
				}
				int x = Integer.parseInt(params.getStringArgument(1));
				int y = Integer.parseInt(params.getStringArgument(2));
				int z = Integer.parseInt(params.getStringArgument(3));

				if (x < 16 || y < 16 || z < 16) {
					player.getActionSender().sendChatMessage("16x16x16 is the smallest level supported");
					return;
				}

				if (!(2*x == (x ^ x-1) + 1) && !(2*y == (y ^ y-1) + 1) && !(2*z == (z ^ z-1) + 1)) {
					player.getActionSender().sendChatMessage("sizes must be powers of 2");
					return;
				}
				String type = params.getStringArgument(4);

				Level newlvl = new Level();
				Builder b;
				if (type.equalsIgnoreCase("Hills")) {
					b = new LandscapeBuilder(newlvl);
				} else if (type.equalsIgnoreCase("Flat")) {
					b = new FlatGrassBuilder(newlvl);
				} else if (type.equalsIgnoreCase("Pixel")) {
					b = new PixelBuilder(newlvl);
				} else {
					player.getActionSender().sendChatMessage("Valid types are 'Hills' 'Flat' 'Pixel'");
					return;
				}

				if (params.getArgumentCount() == 6) {
					theme = params.getStringArgument(5);
				}

				if (theme.equalsIgnoreCase("Summer")) {
					b.setSummer();
				} else if (theme.equalsIgnoreCase("Winter")) {
					b.setWinter();
				} else if (theme.equalsIgnoreCase("Oasis")) {
					b.setOasis();
				} else {
					player.getActionSender().sendChatMessage("Valid themes are 'Summer' 'Winter' 'Oasis'");
					return;
				}

				newlvl.generateLevel(b, x, y, z, new Environment(), name, player.getName());
				LevelManager.save(newlvl);
				player.getActionSender().sendChatMessage("World " + name + " created");
			} catch (Exception e) {
				player.getActionSender().sendChatMessage("/generate <name> <x> <y> <z> <type> [<theme>]");
			}
	}

}
