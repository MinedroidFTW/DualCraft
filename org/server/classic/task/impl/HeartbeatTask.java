package dualcraft.org.server.classic.task.impl;

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

import dualcraft.org.server.classic.Configuration;
import dualcraft.org.server.classic.Constants;
import dualcraft.org.server.classic.heartbeat.HeartbeatManager;
import dualcraft.org.server.classic.task.ScheduledTask;
import dualcraft.org.server.classic.Server;

/**
 * A task which sends a heartbeat periodically to the master server.
 * 
 */
public class HeartbeatTask extends ScheduledTask {
	
	/**
	 * The delay.
	 */
	private static final long DELAY = 45000;
	
	/**
	 * Creates the heartbeat task with a 45s delay.
	 */
	public HeartbeatTask() {
		super(0);
	}


	public void execute() {
		if (this.getDelay() == 0) {
			this.setDelay(DELAY);
		}
		final Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name", Configuration.getConfiguration().getName());
		parameters.put("users", String.valueOf(Server.getServer().getPlayerList().size()));
		parameters.put("max", String.valueOf(Configuration.getConfiguration().getMaximumPlayers()));
		parameters.put("public", String.valueOf(Configuration.getConfiguration().isPublicServer()));
		parameters.put("port", String.valueOf(Configuration.getConfiguration().getPort()));
		parameters.put("salt", String.valueOf(HeartbeatManager.getHeartbeatManager().getSalt()));
		parameters.put("version", String.valueOf(Constants.PROTOCOL_VERSION));
		HeartbeatManager.getHeartbeatManager().sendHeartbeat(parameters);
	}
}
