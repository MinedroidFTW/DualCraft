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

import java.util.Iterator;
import java.util.Set;

import dualcraft.org.server.classic.model.Entity;
import dualcraft.org.server.classic.model.Player;
import dualcraft.org.server.classic.model.World;
import dualcraft.org.server.classic.Server;
import dualcraft.org.server.classic.task.ScheduledTask;
import org.slf4j.*;

/**
 * Updates the players and game world.
 * 
 */
public class UpdateTask extends ScheduledTask {
	
	/**
	 * The delay.
	 */
	private static final long DELAY = 100;

	private static final Logger logger = LoggerFactory.getLogger(UpdateTask.class);
	
	/**
	 * Creates the update task with a delay of 100ms.
	 */
	public UpdateTask() {
		super(DELAY);
	}

	public void execute() {
		World[] worlds = Server.getServer().getWorlds();
		for(World world : worlds) {
			world.getGameMode().tick();
			logger.trace("Player list length: {}", world.getPlayerList().getPlayers().size());
			for (Player player : world.getPlayerList().getPlayers()) {
				Set<Entity> localEntities = player.getLocalEntities();
				Iterator<Entity> localEntitiesIterator = localEntities.iterator();
				while (localEntitiesIterator.hasNext()) {
					Entity localEntity = localEntitiesIterator.next();
					if (localEntity.getId() == -1) {
						localEntitiesIterator.remove();
						logger.trace("Sending removeEntity to other player");
						player.getSession().getActionSender().sendRemoveEntity(localEntity);
					} else {
						logger.trace("Sending updateEntity to other player");
						player.getSession().getActionSender().sendUpdateEntity(localEntity);
					}
				}
				for (Player otherEntity : world.getPlayerList().getPlayers()) {
					if (!localEntities.contains(otherEntity) && otherEntity != player) {
						localEntities.add(otherEntity);
						logger.trace("Sending addEntity to other player");
						player.getSession().getActionSender().sendAddEntity(otherEntity);
					}
				}
			}
			for (Player player : world.getPlayerList().getPlayers()) {
				player.resetOldPositionAndRotation();
			}
			world.getLevel().applyBlockBehaviour();
		}
	}
	
}
