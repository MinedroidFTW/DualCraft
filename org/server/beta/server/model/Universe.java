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
package dualcraft.org.server.beta.server.model;

import dualcraft.org.server.beta.resource.impl.core.universal.SettingsResourceHandler;
import dualcraft.org.server.beta.server.model.world.World;
import dualcraft.org.server.beta.server.model.world.WorldNotFoundException;
import dualcraft.org.server.beta.server.networking.netty.MinecraftServerHandler;
import dualcraft.org.server.beta.server.networking.packet.PacketRegister;
import dualcraft.org.server.beta.server.networking.packet.handlers.LoginHandshakePacketHandler;
import dualcraft.org.server.beta.server.networking.packet.handlers.LoginRequestPacketHandler;
import dualcraft.org.server.beta.server.networking.packet.handlers.ServerListDetailsPacketHandler;
import dualcraft.org.server.beta.task.AbstractRunnableTask;
import dualcraft.org.server.beta.task.impl.universal.generic.ServerSaveTask;
import dualcraft.org.server.beta.task.scheduled.TaskScheduler;
import dualcraft.org.server.beta.task.scheduled.impl.universal.generic.GCTaskRunner;
import dualcraft.org.server.beta.version.Version;
import dualcraft.org.server.beta.version.VersionType;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import jline.console.ConsoleReader;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * A class defining the universe
 *
 * 
 * @version 0.2.2.0
 */
public class Universe {

    /**
     * The universe instance
     *
     * @since 0.1.0.0
     */
    private static Universe instance;
    
    /**
     * Whether the universe was started
     * 
     * @since 0.2.0.2
     */
    private static boolean started = false;

    /**
     * Gets the universe instance
     *
     * @return The universe instance
     * @since 0.1.0.0
     */
    public static Universe getInstance() {
        //Does the universe not exist?
        if (!started) {
            started = true;
            instance = new Universe(); //Create it
        }        //And return it
        return instance;
    }
    
    /**
     * The logger
     *
     * @since 0.1.0.0
     */
    private static final Logger LOGGER = Logger.getLogger("Universe");

    /**
     * dualcraft 's version
     *
     * @since 0.1.0.2
     */
    private final transient Version version = new Version(0, 0, 0, 7, VersionType.STABLE);

    /**
     * Gets dualcraft 's version
     *
     * @return The version
     * @since 0.1.0.2
     */
    public Version getVersion() {
        return this.version;
    }
    /**
     * The universal task scheduler
     *
     * @since 0.1.0.1
     */
    private final transient TaskScheduler universalScheduler = new TaskScheduler(3);

    /**
     * Gets the universal task scheduler
     *
     * @return The task scheduler
     * @since 0.1.0.1
     */
    public TaskScheduler getUniversalScheduler() {
        return this.universalScheduler;
    }
    /**
     * The server bootstrap
     *
     * @since 0.1.0.3
     */
    private transient ServerBootstrap bootstrap;

    /**
     * Get the server bootstrap
     *
     * @return The bootstrap
     * @since 0.1.0.3
     */
    public ServerBootstrap getBootstrap() {
        return this.bootstrap;
    }
    
    /**
     * The map of worlds
     * 
     * @since 0.2.0.0
     */
    private transient Map<String, World> worlds;
    
    /**
     * Gets the map of worlds
     * 
     * @return The worlds
     * @since 0.2.0.0
     */
    public Map<String, World> getWorlds() {
        return this.worlds;
    }
    
    /**
     * Gets the specified world
     * 
     * @param name The name to search for
     * @return The world
     * @throws WorldNotFoundException The world was not found!
     */
    public World getWorld(final String name) throws WorldNotFoundException {
        //Check to see if the world exists
        if (this.worlds.containsKey(name)) {
            //Return the correct world
            return this.worlds.get(name);
        }
        
        //See if it's the default world
        if (name.equals(this.getSetting("defaultWorldName", "earth"))) {
            throw new WorldNotFoundException("Default world not found!");
        }
        
        //Throw a world not found exception
        throw new WorldNotFoundException("World " + name + " not found");
        
    }
    
    /**
     * The map of settings
     *
     * @since 0.1.0.4
     */
    private transient Map<String, Object> settings;

    /**
     * Gets the settings
     * 
     * @return The settings
     * @since 0.1.0.4
     */
    public Map<String, Object> getSettings() {
        return this.settings;
    }

    /**
     * Gets a setting
     * 
     * @param identifier The ID to use
     * @param defaultValue The default value to use
     * @return The setting
     * @since 0.1.0.6
     */
    public Object getSetting(final String identifier, final Object defaultValue) {
        //Check to see if the setting ID is used
        if (!this.settings.containsKey(identifier)) //Nope
        {
            this.settings.put(identifier, defaultValue);
        }

        //Get the value
        return this.settings.get(identifier);

    }
    /**
     * The hash map of unverified usernames
     *
     * @since 0.1.0.8
     */
    private final transient Map<String, String> unverifiedUsernames = new HashMap<String, String>();

    /**
     * Gets the hash map of unverified usernames
     * 
     * @return The unverified usernames map
     * @since 0.1.0.8
     */
    public Map<String, String> getUnverifiedUsernames() {
        return this.unverifiedUsernames;
    }
    /**
     * The secure random
     *
     * @since 0.1.0.7
     */
    private final transient SecureRandom secureRandom = new SecureRandom();

    /**
     * Gets the secure random
     * 
     * @return The secure random
     * @since 0.1.0.7
     */
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }
    /**
     * The next entity ID
     *
     * @since 0.1.0.9
     */
    private transient int nextEntityID = 0;

    /**
     * Gets the next entity ID
     * 
     * @return The next entity ID
     * @since 0.1.0.9
     */
    public int getNextEntityID() {
        return this.nextEntityID++;
    }
    /**
     * The console reader
     *
     * @since 0.1.0.10
     */
    private transient ConsoleReader reader;

    /**
     * Gets the console reader
     * 
     * @return The console reader
     * @since 0.1.0.10
     */
    public ConsoleReader getReader() {
        return this.reader;
    }

    /**
     * Begin processing/loading the universe
     */
    public void begin() {
        try {
            this.reader = new ConsoleReader();
        } catch (IOException ex) {
            LOGGER.severe("Could not load jline");
            throw new RuntimeException("Could not load jLine2!", ex);
        }

        reader.setBellEnabled(false);

        //Set the settings
        this.settings = new SettingsResourceHandler().load();
        
        //Check to see if the current version is unstable
        if (!this.getVersion().isStable()) {
            Logger.getLogger("").setLevel(Level.FINER);
            //Get every global handler
            for (Handler handler : Logger.getLogger("").getHandlers()) {
                //Log everything except FINEST
                handler.setLevel(Level.FINER);
            }
        }

        //And log the startup request
        LOGGER.info("Starting " + this.getSetting("serverName", "dualcraft ") + " version " + this.getVersion());

        //Set up the world hash map
        this.worlds = new HashMap<String, World>();
        
        //TODO Load the worlds here
        
        //TODO Check for and generate the default world here
        
        //Log the worlds loaded
        LOGGER.fine("Loaded " + this.worlds.size() + " " + (this.worlds.size() == 1 ? "world" : "worlds"));
        
        //Add scheduled tasks
        //Add the GC task runner
        this.universalScheduler.schedule(new GCTaskRunner());

        //Log
        LOGGER.info("Scheduled all universal tasks");

        //Start setting up packets
        //Add the login request packet
        PacketRegister.getInstance().registerHandler(0x01, new LoginRequestPacketHandler());
        //Add the login handshake packet
        PacketRegister.getInstance().registerHandler(0x02, new LoginHandshakePacketHandler());
        //Add the server list ping packet
        PacketRegister.getInstance().registerHandler(0xFE, new ServerListDetailsPacketHandler());

        //Log
        LOGGER.info("Registered " + PacketRegister.getInstance().getHandlers().size() + " packet(s)");

        //Add a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {

            /**
             * Runs this shutdown hook
             */
            @Override
            public void run() {
                //Save the server
                AbstractRunnableTask.register(new ServerSaveTask(), false);
            }
        });

        //Set the bootstrap
        this.bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        //Add the Minecraft handler
        this.bootstrap.getPipeline().addFirst("handler", new MinecraftServerHandler());

        //Loop through all of the ports
        for (int port : (List<Integer>) this.getSetting("ports", (List<Integer>) Arrays.asList(25565))) {
            //Bind to the port
            this.bootstrap.bind(new InetSocketAddress(port));
        }

        //Log
        LOGGER.info(this.getSetting("serverName", "dualcraft ") + " has been started");

        //Check to see if usernames aren't to be verified
        if (!(Boolean) Universe.getInstance().getSetting("verifyUsernames", true)) {
            LOGGER.warning("Usernames will not be verified!");
        }
        
        //Check to see if the current version is unstable
        if (!this.getVersion().isStable()) {
            LOGGER.warning("You are using an unstable version of dualcraft !");
        }
        
        String command;
                
        while (true) {
            try {
                command = this.reader.readLine();
            } catch (IOException ex) {
                LOGGER.severe("Error reading command");
                continue;
            }
            //Check the command to see if it's null or blank
            if (command == null || command.equals("")) {
                continue;
            }
            
            LOGGER.info("Console command " + command + " requested");
        }

    }
}
