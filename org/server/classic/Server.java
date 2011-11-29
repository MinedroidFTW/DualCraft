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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Collection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.lang.ref.SoftReference;

import java.io.File;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import dualcraft.org.server.classic.model.World;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.net.SessionHandler;
import dualcraft.org.server.classic.task.TaskQueue;
import dualcraft.org.server.classic.task.impl.HeartbeatTask;
import dualcraft.org.server.classic.task.impl.UpdateTask;
import dualcraft.org.server.classic.task.impl.FListHeartbeatTask;
import dualcraft.org.server.classic.heartbeat.HeartbeatManager;
import dualcraft.org.server.classic.util.SetManager;
import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.util.PlayerList;
import dualcraft.org.server.classic.model.Player;
import dualcraft.org.server.classic.persistence.SavedGameManager;
import dualcraft.org.server.classic.persistence.SavePersistenceRequest;
import dualcraft.org.server.classic.security.Group;
import dualcraft.org.server.classic.security.Permission;
import dualcraft.org.server.classic.security.Principal;
import dualcraft.org.server.classic.security.Policy;
import dualcraft.org.server.classic.io.LevelManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.ArrayList;
import java.text.ParseException;
import org.slf4j.*;


/**
 * The core class of the DualCraft server.
 * 
 */
public final class Server {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	private static final Logger m_loginLogger = LoggerFactory.getLogger(Server.class.getName()+".Logins");

	private final PlayerList m_players;


	public static boolean bootstrap() {
		File dataDir = new File("data/");
		if (!dataDir.exists()) {
			logger.info("Extracting resources from jar file.");
			InputStream bootstrapZip = Server.class.getResourceAsStream("/bootstrap.zip");
			if (bootstrapZip != null) {
				dataDir.mkdir();

				ZipInputStream bootstrap = new ZipInputStream(bootstrapZip);

				ZipEntry current;
				try {
					while((current = bootstrap.getNextEntry()) != null) {
						logger.info("Extracting data/{}", current.getName());
						File dest = new File("data/"+current.getName());
						if (current.isDirectory()) {
							dest.mkdirs();
							continue;
						}
						File dir = dest.getParentFile();
						if (dir != null)
							dir.mkdirs();
						FileOutputStream out = new FileOutputStream(dest);
						for(int c = bootstrap.read(); c != -1;c = bootstrap.read())
							out.write(c);
						bootstrap.closeEntry();
						out.close();
					}
					bootstrap.close();
				} catch (IOException e) {
					logger.error("Bootstrap.zip is corrupt.", e);
					return false;
				}
			} else {
				logger.error("No bootstrap.zip found within the jar. Make sure you downloaded the right version!");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * The entry point of the server application.
	 * @param args
	 */
	public static void main(String[] args) {
		if (bootstrap()) {
			try {
				INSTANCE = new Server();
				INSTANCE.start();
			} catch (Throwable t) {
				logger.error("An error occurred whilst loading the server.", t);
			}
		} else {
			logger.error("Bootstrap failed.");
		}
	}

	private HashMap<String,SoftReference<World>> m_worlds;

	private static Server INSTANCE;

	public static Server getServer() {
		return INSTANCE;
	}

	public World[] getWorlds() {
		World[] ret = new World[m_worlds.size()];
		SoftReference[] refs = new SoftReference[m_worlds.size()];
		refs = m_worlds.values().toArray(refs);
		for(int i = 0;i < m_worlds.size();i++) {
			ret[i] = (World)refs[i].get();
		}
		return ret;
	}

	public String[] getLoadedWorldNames() {
		String[] ret = new String[m_worlds.size()];
		SoftReference[] refs = new SoftReference[m_worlds.size()];
		refs = m_worlds.values().toArray(refs);
		for(int i = 0;i < m_worlds.size();i++) {
			ret[i] = ((World)refs[i].get()).getName();
		}
		return ret;
	}

	/**
	 * The socket acceptor.
	 */
	private final IoAcceptor acceptor = new NioSocketAcceptor();
	
	/**
	 * Creates the server.
	 * @throws IOException if an I/O error occurs.
	 * @throws FileNotFoundException if the configuration file is not found.
	 */
	public Server() throws IOException { 
		logger.info("Starting DualCraft server...");
		logger.info("Configuring...");
		Configuration.readConfiguration();
		SetManager.getSetManager().reloadSets();
		m_players = new PlayerList();
		
		acceptor.setHandler(new SessionHandler());
		logger.info("Initializing games...");
		m_worlds = new HashMap<String,SoftReference<World>>();
		if (!loadWorld(Configuration.getConfiguration().getDefaultMap())) {
			logger.info("Generating default 256x256x64 world.");
			Level lvl = new Level();
			lvl.generateLevel();
			lvl.setName(Configuration.getConfiguration().getDefaultMap());
			LevelManager.save(lvl);
			boolean loaded = loadWorld(Configuration.getConfiguration().getDefaultMap());
			assert(loaded);
		}
	}

	public boolean loadWorld(String name) {
		if (m_worlds.containsKey(name)) {
			logger.trace("World {} was already loaded some time ago.", name);
			if (m_worlds.get(name).get() == null) {
				logger.debug("Found expired world {}. Removing.", name);
				m_worlds.remove(name);
			} else {
				logger.trace("Found cached world {}.", name);
				return true;
			}
		}
		try {
			logger.info("Loading level \""+name+"\"");
			Level lvl = LevelManager.load(name);
			if (lvl == null)
				return false;
			World w = new World(lvl);
			logger.info("Loading policy for {}...", w);
			try {
				w.setPolicy(new Policy(w.getName(), new BufferedReader(new FileReader("data/DualCraft.permissions"))));
			} catch (ParseException e) {
				logger.warn("Error parsing policy, line "+e.getErrorOffset(), e);
			} catch (IOException e) {
				logger.warn("Error reading policy", e);
			}
			m_worlds.put(name, new SoftReference<World>(w));
			return true;
		} catch (InstantiationException e) {
			logger.error("Error loading world.", e);
		} catch (IllegalAccessException e) {
			logger.error("Error loading world.", e);
		} catch (ClassNotFoundException e) {
			logger.error("Error loading world.", e);
		} catch (IOException e) {
			logger.error("Error loading world.", e);
		}
		return false;
	}

	public boolean unloadLevel(String name) {
		logger.info("Unloading level \""+name+"\"");
		final Configuration c = Configuration.getConfiguration();
		if (m_worlds.containsKey(name) && !name.equalsIgnoreCase(c.getDefaultMap())) {
			World w = m_worlds.get(name).get();
			w.finalize();

			Collection<Player> players = w.getPlayerList().getPlayers();
			for (Player p : players) {
				p.moveToWorld(getWorld(c.getDefaultMap()));
			}

			m_worlds.remove(name);
			return true;
		} else {
			logger.trace("World {} is not loaded.", name);
		}
		return false;
	}

	public World getWorld(String name) {
		loadWorld(name);
		World w = m_worlds.get(name).get();
		return w;
	}

	public boolean hasWorld(String name) {
		try {
			if (m_worlds.get(name).get() != null) {
				return true;
			}
		} catch (Exception e) { }
		return false;
	}

	/**
	 * Starts the server.
	 * @throws IOException if an I/O error occurs.
	 */
	public void start() throws IOException {
		logger.info("Initializing server...");	
		logger.info("Starting tasks");
		TaskQueue.getTaskQueue().schedule(new UpdateTask());
		TaskQueue.getTaskQueue().schedule(new HeartbeatTask());
		if (Configuration.getConfiguration().getUseFList())
			TaskQueue.getTaskQueue().schedule(new FListHeartbeatTask());
		logger.info("Binding to port " + Configuration.getConfiguration().getPort() + "...");
		acceptor.bind(new InetSocketAddress(Configuration.getConfiguration().getPort()));
		logger.info("Ready for connections.");
	}

	/**
	 * Registers a session.
	 * @param session The session.
	 * @param username The username.
	 * @param verificationKey The verification key.
	 */
	public void register(MinecraftSession session, String username, String verificationKey) {
		// check if the player is banned
		try {
			File banned = new File("data/banned.txt");
			Scanner fread = new Scanner(banned);
			while (fread.hasNextLine()) {
				if (username.equalsIgnoreCase(fread.nextLine())) {
					session.getActionSender().sendLoginFailure("Banned.");
					logger.info("Refused {}: {} is banned.", session, username);
					break;
				}
			}
			fread.close();
		} catch (IOException e) { }

		// verify name
		if (Configuration.getConfiguration().isVerifyingNames()) {
			if (verificationKey.equals("--")) {
				session.getActionSender().sendLoginFailure("Cannot verify names with ip= based URLs");
			}
			long salt = HeartbeatManager.getHeartbeatManager().getSalt();
			String hash = new StringBuilder().append(String.valueOf(salt)).append(username).toString();
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("No MD5 algorithm!");
			}
			digest.update(hash.getBytes());
			String test = new BigInteger(1, digest.digest()).toString(16);
			if (!verificationKey.equals(test)) {
				session.getActionSender().sendLoginFailure("Illegal name.");
				return;
			}
		}
		// check if name is valid
		char[] nameChars = username.toCharArray();
		for (char nameChar : nameChars) {
			if (nameChar < ' ' || nameChar > '\177') {
				session.getActionSender().sendLoginFailure("Invalid name!");
				logger.info("Refused {}, invalid login name.", username);
				return;
			}
		}
		// disconnect any existing players with the same name
		for (Player p : m_players.getPlayers()) {
			if (p.getName().equalsIgnoreCase(username)) {
				// Should it not be the person attempting to connect who gets dropped?
				// FIXME
				p.getSession().getActionSender().sendLoginFailure("Logged in from another computer.");
				logger.info("Kicked {}, logged in from another computer.", p);
				break;
			}
		}
		// attempt to add the player
		final Player player = new Player(session, username);
		if (!m_players.add(player)) {
			player.getSession().getActionSender().sendLoginFailure("Too many players online!");
			logger.warn("Too many players online!");
			return;
		}
		// final setup
		m_loginLogger.info("LOGIN "+player.getName()+" "+session.getAddress().toString());

		session.setPlayer(player);
		final Configuration c = Configuration.getConfiguration();
		session.getActionSender().sendLoginResponse(Constants.PROTOCOL_VERSION, c.getName(), c.getMessage(), false);
		assert(getWorld(c.getDefaultMap()) != null);
		assert(session.getPlayer() == player);
		logger.debug("Moving player to default world");
		player.moveToWorld(getWorld(c.getDefaultMap()));
		m_loginLogger.info("JOIN "+player.getName()+" "+player.getWorld().getLevel().getName());
	}

	public PlayerList getPlayerList() {
		return m_players;
	}

	/**
	 * Unregisters a session.
	 * @param session The session.
	 */
	public void unregister(MinecraftSession session) {
		if (session.isAuthenticated()) {
			logger.trace("Unregistering session.");
			World w = session.getPlayer().getWorld();
			w.removePlayer(session.getPlayer());
			m_players.remove(session.getPlayer());
			SavedGameManager.getSavedGameManager().queuePersistenceRequest(new SavePersistenceRequest(session.getPlayer()));
			session.setPlayer(null);
		}
	}
}
