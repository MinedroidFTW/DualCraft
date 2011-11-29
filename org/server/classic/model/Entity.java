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

import java.util.HashSet;
import java.util.Set;

/**
 * The superclass for players and mobs.
 * 
 */
public abstract class Entity {
	
	/**
	 * A collection of local entities.
	 */
	private final Set<Entity> localEntities = new HashSet<Entity>();
	private Position oldPosition;
	private Position position;
	private Rotation oldRotation;
	private Rotation rotation;
	private int id = -1;
	private int oldId = -1;
	
	/**
	 * Default public constructor.
	 */
	public Entity() {
		position = new Position(0, 0, 0);
		rotation = new Rotation(0, 0);
		resetOldPositionAndRotation();
	}
	
	/**
	 * Gets the local entity set.
	 * @return The local entity set.
	 */
	public Set<Entity> getLocalEntities() {
		return localEntities;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * @param id The id.
	 */
	public void setId(int id) {
		if (id == -1) {
			this.oldId = this.id;
		}
		this.id = id;
	}
	
	/**
	 * Gets the old id.
	 * @return The old id.
	 */
	public int getOldId() {
		return oldId;
	}
	
	/**
	 * Sets the rotation.
	 * @param rotation The rotation.
	 */
	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Gets the rotation.
	 * @return The rotation.
	 */
	public Rotation getRotation() {
		return rotation;
	}
	
	/**
	 * Sets the position.
	 * @param position The position.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Gets the position.
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Gets the old position.
	 * @return The old position.
	 */
	public Position getOldPosition() {
		return oldPosition;
	}
	
	/**
	 * Gets the old rotation.
	 * @return The old rotation.
	 */
	public Rotation getOldRotation() {
		return oldRotation;
	}
	
	/**
	 * Resets the old position and rotation data.
	 */
	public void resetOldPositionAndRotation() {
		oldPosition = position;
		oldRotation = rotation;
	}
	
	/**
	 * Gets the name of this entity.
	 * @return The name of this entity.
	 */
	public abstract String getName();
	
}
