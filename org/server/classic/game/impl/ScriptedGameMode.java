package dualcraft.org.server.classic.game.impl;

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
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.*;

import dualcraft.org.server.classic.Configuration;
import dualcraft.org.server.classic.game.GameModeAdapter;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Player;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 * A game mode which delegates methods to a script.
 * 
 *
 */
public class ScriptedGameMode extends GameModeAdapter<Player> {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ScriptedGameMode.class);
	
	/**
	 * The python interpreter.
	 * We were origianlly using javax.script but that had some issues with
	 * commands.
	 */
	private PythonInterpreter interpreter = new PythonInterpreter();
	
	/**
	 * Creates the scripted game mode.
	 * @throws IOException if an I/O error occurs.
	 */
	public ScriptedGameMode() throws IOException {
		init();
	}
	
	/**
	 * Initializes the script engine and evaluates the script.
	 * @throws IOException if an I/O error occurs.
	 */
	private void init() throws IOException {
		String name = Configuration.getConfiguration().getScriptName();
		
		logger.debug("Evaluating script...");
		InputStream is = new FileInputStream("./data/scripts/" + name);
		try {
			interpreter.execfile(is);
		} finally {
			is.close();
		}
		
		logger.debug("Initializing script...");
		delegate("init", this);
	}
	
	/**
	 * Delegates a call to the engine.
	 * @param method The method name.
	 * @param args The arguments.
	 */
	private boolean delegate(String method, Object... args) {
		PyObject m = interpreter.get(method);
		if(m != null) {
			try {
				//FIXME: Why doesn't this compile after using system libraries?
				//m.__call__(Py.javas2pys(args));
			} catch (Exception ex) {
				logger.error("Error invoking method.", ex);
			}
		} else {
			return false;
		}
		return true;
	}
	
	@Override
	public void playerConnected(Player player) {
		if(!delegate("playerConnected", player)) {
			super.playerConnected(player);
		}
	}
	
	@Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
		if(!delegate("setBlock", player, level, x, y, z, mode, type)) {
			super.setBlock(player, level, x, y, z, mode, type);
		}
	}
	
	@Override
	public void playerDisconnected(Player player) {
		if(!delegate("playerDisconnected", player)) {
			super.playerDisconnected(player);
		}
	}
	
	@Override
	public void broadcastChatMessage(Player player, String message) {
		if(!delegate("broadcastChatMessage", player, message)) {
			super.broadcastChatMessage(player, message);
		}
	}
	
	@Override
	public void tick() {
		if(!delegate("tick")) {
			super.tick();
		}
	}
	
}
