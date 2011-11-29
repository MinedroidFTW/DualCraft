package dualcraft.org.server.classic.model;

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

import dualcraft.org.server.classic.Configuration;
import dualcraft.org.server.classic.task.impl.SaveLevelTask;
import dualcraft.org.server.classic.task.TaskQueue;
import dualcraft.org.server.classic.game.GameMode;
import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.util.PlayerList;
import dualcraft.org.server.classic.io.LevelManager;
import dualcraft.org.server.classic.security.Policy;
import java.io.IOException;
import org.slf4j.*;

/**
 * Manages the in-game world.
 * 
 */
public final class World {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(World.class);

	private Policy m_policy;

	public Policy getPolicy() {
		return m_policy;
	}

	public void setPolicy(Policy p) {
		m_policy = p;
	}
	
	
	/**
	 * The level.
	 */
	private Level level;
	
	/**
	 * The player list.
	 */
	private PlayerList playerList = new PlayerList();
	
	/**
	 * The game mode.
	 */
	private GameMode<Player> gameMode;

	public String getName() {
		return level.getName();
	}
	
	/**
	 * Default private constructor.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */

	public World(Level lvl) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		m_policy = new Policy();
		gameMode = (GameMode) Class.forName(Configuration.getConfiguration().getGameMode()).newInstance();
		level = lvl;
		level.setOnBlockChangeHandler(new OnBlockChangeHandler() {
			public void onBlockChange(int x, int y, int z) {
				for (Player player : getPlayerList().getPlayers()) {
					player.getSession().getActionSender().sendBlock(x, y, z, level.getBlock(x, y, z));
				}
			}
		});
		TaskQueue.getTaskQueue().schedule(new SaveLevelTask(level));
		logger.info("Active game mode : " + gameMode.getClass().getName() + ".");
	}

	public void finalize() {
		logger.info("Finalizing world, saving level.");
		LevelManager.save(level);
	}
	
	/**
	 * Gets the current game mode.
	 * @return The current game mode.
	 */
	public GameMode<Player> getGameMode() {
		return gameMode;
	}

	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public PlayerList getPlayerList() {
		return playerList;
	}
	
	/**
	 * Gets the level.
	 * @return The level.
	 */
	public Level getLevel() {
		return level;
	}

	public void removePlayer(Player p) {
		logger.trace("Removing player");
		playerList.remove(p);
		getGameMode().playerDisconnected(p);
	}

	public void addPlayer(Player p) {
		playerList.add(p);
		//getGameMode().playerConnected(p);
	}
	
	/**
	 * Completes registration of a session.
	 * @param session The session.
	 */
	public void completeRegistration(MinecraftSession session) {
		if (!session.isAuthenticated()) {
			session.close();
			return;
		}
		session.getActionSender().sendChatMessage("Welcome to DualCraft!");
		// Notify game mode
		getGameMode().playerConnected(session.getPlayer());
	}
	
	/**
	 * Broadcasts a chat message.
	 * @param player The source player.
	 * @param message The message.
	 */
	public void broadcast(Player player, String message) {
		for (Player otherPlayer : playerList.getPlayers()) {
			otherPlayer.getSession().getActionSender().sendChatMessage(player.getId(), message);
		}
	}
	
	/**
	 * Broadcasts a server message.
	 * @param message The message.
	 */
	public void broadcast(String message) {
		for (Player player : playerList.getPlayers()) {
			player.getSession().getActionSender().sendChatMessage(message);
		}
	}
	
}
