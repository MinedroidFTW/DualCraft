package dualcraft.org.server.classic;

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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import dualcraft.org.server.classic.game.impl.CreativeGameMode;

/**
 * Manages server configuration.
 * 
 */
public class Configuration {
	
	/**
	 * The configuration instance.
	 */
	private static Configuration configuration;
	
	/**
	 * Reads and parses the configuration.
	 * @throws FileNotFoundException if the configuration file is not present.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void readConfiguration() throws FileNotFoundException, IOException {
		synchronized (Configuration.class) {
			Properties props = new Properties();
			InputStream is = new FileInputStream("./data/DualCraft.properties");
			try {
				props.load(is);
				configuration = new Configuration(props);
			} finally {
				is.close();
			}
		}
	}
	
	/**
	 * Gets the configuration instance.
	 * @return The configuration instance.
	 */
	public static Configuration getConfiguration() {
		synchronized (Configuration.class) {
			return configuration;
		}
	}
	
	/**
	 * The server name.
	 */
	private String name;

	/**
	 * The server port.
	 */
	private int port;
	
	/**
	 * The server MOTD.
	 */
	private String message;
	
	/**
	 * The maximum allowed player count.
	 */
	private int maximumPlayers;
	
	/**
	 * The radius of a sponge's effectiveness.
	 */
	private int spongeRadius;
	
	/**
	 * Public server flag.
	 */
	private boolean publicServer;
	
	/**
	 * Verify names flag.
	 */
	private boolean verifyNames;
	
	/**
	 * The game mode.
	 */
	private String gameMode;
	
	/**
	 * The script name.
	 */
	private String scriptName;
	
	/**
	 * Creates the configuration from the specified properties object.
	 * @param props The properties object.
	 */
	public Configuration(Properties props) {
		name = props.getProperty("name", "DualCraft Server");
		port = Integer.valueOf(props.getProperty("port", "25565"));
		message = props.getProperty("message", "http://DualCraft.sf.net/");
		maximumPlayers = Integer.valueOf(props.getProperty("max_players", "16"));
		publicServer = Boolean.valueOf(props.getProperty("public", "false"));
		verifyNames = Boolean.valueOf(props.getProperty("verify_names", "false"));
		spongeRadius = Integer.valueOf(props.getProperty("sponge_radius", "2"));
		gameMode = props.getProperty("game_mode", CreativeGameMode.class.getName());
		scriptName = props.getProperty("script_name", null);
		defaultMap = props.getProperty("defaultMap", "default");
		useFList = Boolean.valueOf(props.getProperty("useFList", "false"));
		backupCount = Integer.valueOf(props.getProperty("backupCount", "-1"));
		backupPeriod = Integer.valueOf(props.getProperty("backupPeriod", "30"));
	}

	private int backupPeriod;

	public int getBackupPeriod() {
		return backupPeriod;
	}

	private int backupCount;

	public int getBackupCount() {
		return backupCount;
	}

	private boolean useFList;

	public boolean getUseFList() {
		return useFList;
	}

	private String defaultMap;

	public String getDefaultMap() {
		return defaultMap;
	}
	
	/**
	 * Gets the server name.
	 * @return The server name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the server port.
	 * @return The server port.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Gets the server MOTD.
	 * @return The server MOTD.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Gets the maximum player count.
	 * @return The maximum player count.
	 */
	public int getMaximumPlayers() {
		return maximumPlayers;
	}
	
	/**
	 * Gets the public server flag.
	 * @return The public server flag.
	 */
	public boolean isPublicServer() {
		return publicServer;
	}
	
	/**
	 * Gets the verify names flag.
	 * @return The verify names flag.
	 */
	public boolean isVerifyingNames() {
		return verifyNames;
	}
	
	
	/**
	 * Gets the range at which a sponge is effective.
	 * @return The sponge radius.
	 */
	public int getSpongeRadius() {
		return spongeRadius;
	}
	
	/**
	 * Gets the game mode class.
	 * @return The game mode class.
	 */
	public String getGameMode() {
		return gameMode;
	}
	
	/**
	 * Gets the script name.
	 * @return The script name.
	 */
	public String getScriptName() {
		return scriptName;
	}
	
}
