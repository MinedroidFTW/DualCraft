package dualcraft.org.server.classic.net;

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


import dualcraft.org.server.classic.model.Entity;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Position;
import dualcraft.org.server.classic.model.Rotation;
import dualcraft.org.server.classic.net.packet.PacketBuilder;

import dualcraft.org.server.classic.persistence.LoadPersistenceRequest;
import dualcraft.org.server.classic.persistence.SavedGameManager;
import dualcraft.org.server.classic.task.Task;
import dualcraft.org.server.classic.task.TaskQueue;
import org.slf4j.*;


/**
 * A utility class for sending packets.
 * 
 */
public class ActionSender {
	
	/**
	 * The session.
	 */
	private MinecraftSession session;

	private static final Logger logger = LoggerFactory.getLogger(ActionSender.class);
	
	/**
	 * Creates the action sender.
	 * @param session The session.
	 */
	public ActionSender(MinecraftSession session) {
		this.session = session;
	}
	
	/**
	 * Sends a login response.
	 * @param protocolVersion The protocol version.
	 * @param name The server name.
	 * @param message The server message of the day.
	 * @param op Operator flag.
	 */
	public void sendLoginResponse(int protocolVersion, String name, String message, boolean op) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(0));
		bldr.putByte("protocol_version", protocolVersion);
		bldr.putString("server_name", name);
		bldr.putString("server_message", message);
		bldr.putByte("user_type", op ? 100 : 0);
		logger.trace("Sending login response");
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a login failure.
	 * @param message The message to send to the client.
	 */
	public void sendLoginFailure(String message) {
		logger.info("Login failure: {}", message);
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(14));
		bldr.putString("reason", message);
		logger.trace("Sending login failure");
		session.send(bldr.toPacket());
		session.close();
	}
	
	/**
	 * Sends the level init packet.
	 */
	public void sendLevelInit() {
		session.setAuthenticated();
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(2));
		logger.trace("Sending level init");
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a level block/chunk.
	 * @param len The length of the chunk.
	 * @param chunk The chunk data.
	 * @param percent The percentage.
	 */
	public void sendLevelBlock(int len, byte[] chunk, int percent) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(3));
		bldr.putShort("chunk_length", len);
		bldr.putByteArray("chunk_data", chunk);
		bldr.putByte("percent", percent);
		logger.trace("Sending block");
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends the level finish packet.
	 */
	public void sendLevelFinish() {
		TaskQueue.getTaskQueue().push(new Task() {
			public void execute() {
				// for thread safety
				Level level = session.getPlayer().getWorld().getLevel();
				PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(4));
				bldr.putShort("width", level.getWidth());
				bldr.putShort("height", level.getHeight());
				bldr.putShort("depth", level.getDepth());
				logger.trace("Sending level finish");
				session.send(bldr.toPacket());
				sendAddEntity(session.getPlayer(), -1);
				sendTeleport(level.getSpawnPosition(), level.getSpawnRotation());
				// now load the player's game (TODO in the future do this in parallel with loading the level)
				// TODO: We should use this to save what world a player was last
				// on
				SavedGameManager.getSavedGameManager().queuePersistenceRequest(new LoadPersistenceRequest(session.getPlayer()));
			}
		});
	}
	
	/**
	 * Sends a teleport.
	 * @param position The new position.
	 * @param rotation The new rotation.
	 */
	public void sendTeleport(Position position, Rotation rotation) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(8));
		bldr.putByte("id", -1);
		bldr.putShort("x", position.getX());
		bldr.putShort("y", position.getY());
		bldr.putShort("z", position.getZ());
		bldr.putByte("rotation", rotation.getRotation());
		bldr.putByte("look", rotation.getLook());
		logger.trace("Sending teleport");
		session.send(bldr.toPacket());
	}

	public void sendAddEntity(Entity entity, int id) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(7));
		bldr.putByte("id", id);
		bldr.putString("name", entity.getName());
		bldr.putShort("x", entity.getPosition().getX());
		bldr.putShort("y", entity.getPosition().getY());
		bldr.putShort("z", entity.getPosition().getZ());
		bldr.putByte("rotation", entity.getRotation().getRotation());
		bldr.putByte("look", entity.getRotation().getLook());
		logger.trace("Sending add entity");
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends the add entity packet.
	 * @param entity The entity being added.
	 */
	public void sendAddEntity(Entity entity) {
		sendAddEntity(entity, entity.getId());
	}
	
	/**
	 * Sends the update entity packet.
	 * @param entity The entity being updated.
	 */
	public void sendUpdateEntity(Entity entity) {
		final Position oldPosition = entity.getOldPosition();
		final Position position = entity.getPosition();
		
		final Rotation oldRotation = entity.getOldRotation();
		final Rotation rotation = entity.getRotation();
		
		final int deltaX = -oldPosition.getX() - position.getX();
		final int deltaY = -oldPosition.getY() - position.getY();
		final int deltaZ = -oldPosition.getZ() - position.getZ();
		
		final int deltaRotation = -oldRotation.getRotation() - rotation.getRotation();
		final int deltaLook = -oldRotation.getLook() - rotation.getLook();
		logger.trace("Sending update entity");
		
		if (deltaX > Byte.MAX_VALUE || deltaX < Byte.MIN_VALUE || deltaY > Byte.MAX_VALUE || deltaY < Byte.MIN_VALUE || deltaZ > Byte.MAX_VALUE || deltaZ < Byte.MIN_VALUE || deltaRotation > Byte.MAX_VALUE || deltaRotation < Byte.MIN_VALUE || deltaLook > Byte.MAX_VALUE || deltaLook < Byte.MIN_VALUE) {
			// teleport
			PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(8));
			bldr.putByte("id", entity.getId());
			bldr.putShort("x", position.getX());
			bldr.putShort("y", position.getY());
			bldr.putShort("z", position.getZ());
			bldr.putByte("rotation", rotation.getRotation());
			bldr.putByte("look", rotation.getLook());
			session.send(bldr.toPacket());
		} else {
			// send move and rotate packet
			PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(9));
			bldr.putByte("id", entity.getId());
			bldr.putByte("delta_x", deltaX);
			bldr.putByte("delta_y", deltaY);
			bldr.putByte("delta_z", deltaZ);
			bldr.putByte("delta_rotation", deltaRotation);
			bldr.putByte("delta_look", deltaLook);
			session.send(bldr.toPacket());
		}
	}
	
	/**
	 * Sends the remove entity packet.
	 * @param entity The entity being removed.
	 */
	public void sendRemoveEntity(Entity entity) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(12));
		bldr.putByte("id", entity.getOldId());
		logger.trace("Sending remove entity");
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a block.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param z Z coordinate.
	 * @param type BlockDefinition type.
	 */
	public void sendBlock(int x, int y, int z, byte type) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(6));
		bldr.putShort("x", x);
		bldr.putShort("y", y);
		bldr.putShort("z", z);
		bldr.putByte("type", type);
		logger.trace("Sending block");
		session.send(bldr.toPacket());
	}
	
	/**
	 * Sends a chat message.
	 * @param message The message.
	 */
	public void sendChatMessage(String message) {
			sendChatMessage(-1, message);
	}

	/**
	 * Sends a chat message.
	 * @param id The source player id.
	 * @param message The message.
	 */
	public void sendChatMessage(int id, String message) {
		PacketBuilder bldr = new PacketBuilder(PersistingPacketManager.getPacketManager().getOutgoingPacket(13));
		bldr.putByte("id", id);
		bldr.putString("message", message);
		logger.trace("Sending chat message: {}", message);
		session.send(bldr.toPacket());
	}
	
}
