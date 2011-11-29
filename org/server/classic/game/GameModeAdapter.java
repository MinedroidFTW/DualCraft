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

import java.util.HashMap;
import java.util.Map;

import dualcraft.org.server.classic.cmd.Command;
import dualcraft.org.server.classic.cmd.impl.*;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Player;
import dualcraft.org.server.classic.security.Permission;
import org.slf4j.*;

/**
 * An implementation of a game mode that does the majority of the work for the
 * game mode developer.
 * 
 */
public abstract class GameModeAdapter<P extends Player> implements GameMode<P> {
	
	/**
	 * The command map.
	 */
	private final Map<String, Command> commands = new HashMap<String, Command>();
	
	/**
	 * Creates the game mode adapter with default settings.
	 */
	public GameModeAdapter() {
		// these commands are standard to every game mode
		registerCommand(SayCommand.getCommand());
		registerCommand(KickCommand.getCommand());
		registerCommand(TeleportCommand.getCommand());
		registerCommand(SetspawnCommand.getCommand());
		registerCommand(SummonCommand.getCommand());
		registerCommand(SpawnCommand.getCommand());
		registerCommand(HelpCommand.getCommand());
		registerCommand(GotoCommand.getCommand());
		registerCommand(MeCommand.getCommand());
		registerCommand(RollCommand.getCommand());
		registerCommand(GenerateCommand.getCommand());
		registerCommand(LevelsCommand.getCommand());
		registerCommand(LoadCommand.getCommand());
		registerCommand(UnloadCommand.getCommand());
		registerCommand(PingCommand.getCommand());
	}

	private static final Logger logger = LoggerFactory.getLogger(GameModeAdapter.class);
	
	/**
	 * Adds a command
	 * @param name The command name.
	 * @param command The command.
	 */
	public void registerCommand(Command command, String name) {
		commands.put(name, command);
	}

	public void registerCommand(Command command) {
		commands.put(command.name(), command);
	}

	/**
	 * Lists all the commands for use by a command like /help
	 */
	//TODO: Alphabatize?
	public String listCommands() {
		String cmds = "";
		for(String key : commands.keySet()) {
			cmds += "/" + key + ", ";
		}
		// the last chars are ", " and are unneeded
		return cmds.substring(0, cmds.length()-2);
	}
	
	public Map<String, Command> getCommands() {
		return commands;
	}
	
	// Default implementation
	public void tick() {
		
	}
	
	// Default implementation
	public void playerConnected(Player player) {
		player.getWorld().broadcast(player.getName()+" joined");
	}
	
	// Default implementation
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
		logger.trace("Setting block mode {} type {}", mode, type);
		if (mode == 1 && !player.isAuthorized(Permission.BUILD)) {
			logger.trace("Not permitted to build.");
			player.getSession().getActionSender().sendBlock(x, y, z, level.getBlock(x, y, z));
		} else if (mode == 0 && !player.isAuthorized(Permission.DESTROY)) {
			logger.trace("Not permitted to destroy.");
			player.getSession().getActionSender().sendBlock(x, y, z, level.getBlock(x, y, z));
		} else {
			logger.trace("Building is OK!");
			level.setBlock(x, y, z, (byte) (mode == 1 ? type : 0));
		}
	}
	
	// Default implementation
	public void playerDisconnected(Player player) {
		player.getWorld().broadcast(player.getName() + " disconnected.");
	}
	
	// Default implementation
	public void broadcastChatMessage(Player player, String message) {
		player.getWorld().broadcast(player, player.getName() + ": " + message);
	}
	
}
