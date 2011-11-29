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

import java.util.HashMap;
import java.util.Map;

import dualcraft.org.server.classic.net.ActionSender;
import dualcraft.org.server.classic.net.MinecraftSession;
import dualcraft.org.server.classic.io.LevelGzipper;
import dualcraft.org.server.classic.security.Group;
import dualcraft.org.server.classic.security.Principal;
import dualcraft.org.server.classic.security.Permission;
import java.util.ArrayList;
import org.slf4j.*;

/**
 * Represents a connected player.
 * 
 */
public class Player extends Entity implements Principal {
	
	/**
	 * The player's session.
	 */
	private final MinecraftSession session;

	private static final Logger logger = LoggerFactory.getLogger(Player.class);
	
	private ArrayList<Permission> m_permissions = new ArrayList<Permission>();

	/**
	 * The player's name.
	 */
	private final String name;
	
	/**
	 * A map of attributes that can be attached to this player.
	 */
	private final Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * Creates the player.
	 * @param name The player's name.
	 */
	public Player(MinecraftSession session, String name) {
		this.session = session;
		this.name = name;
	}

	public String toString() {
		return getName();
	}
	
	/**
	 * Sets an attribute of this player.
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 * @return The old value of the attribute, or <code>null</code> if there was
	 * no previous attribute with that name.
	 */
	public Object setAttribute(String name, Object value) {
		return attributes.put(name, value);
	}
	
	/**
	 * Gets an attribute.
	 * @param name The name of the attribute.
	 * @return The attribute, or <code>null</code> if there is not an attribute
	 * with that name.
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
	
	/**
	 * Checks if an attribute is set.
	 * @param name The name of the attribute.
	 * @return <code>true</code> if set, <code>false</code> if not.
	 */
	public boolean isAttributeSet(String name) {
		return attributes.containsKey(name);
	}
	
	/**
	 * Removes an attribute.
	 * @param name The name of the attribute.
	 * @return The old value of the attribute, or <code>null</code> if an
	 * attribute with that name did not exist.
	 */
	public Object removeAttribute(String name) {
		return attributes.remove(name);
	}

	public boolean equals(Object another) {
		if (another instanceof Player) {
			Player p = (Player) another;
			return getName().equals(p.getName());
		}
		return false;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public Permission[] getPermissions() {
		return m_permissions.toArray(new Permission[m_permissions.size()]);
	}

	public void addPermission(Permission p) {
		m_permissions.add(p);
	}

	public void clearPolicy() {
		m_permissions = new ArrayList<Permission>();
	}

	public boolean isAuthorized(Permission perm) {
		for(Permission p : m_permissions) {
			logger.trace("Testing {} against user permission {}", perm, p);
			if (p.implies(perm)) {
				logger.trace("{} granted to player directly.", perm);
				return true;
			}
		}
		for(Group group : getWorld().getPolicy().getGroups()) {
			if (group.hasMember(this)) {
				logger.trace("Testing group {} for {}", group, perm);
				if (group.isAuthorized(perm)) {
					logger.trace("Authorized by {}", group);
					return true;
				}
			}
		}
		logger.trace("Not authorized for {}", perm);
		return false;
	}
	
	/**
	 * Gets the player's session.
	 * @return The session.
	 */
	public MinecraftSession getSession() {
		return session;
	}
	
	/**
	 * Gets this player's action sender.
	 * @return The action sender.
	 */
	public ActionSender getActionSender() {
		return session.getActionSender();
	}

	/**
	 * Gets the attributes map.
	 * @return The attributes map.
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void teleport(Position position, Rotation rotation) {
		setPosition(position);
		setRotation(rotation);
		session.getActionSender().sendTeleport(position, rotation);
	}

	private World m_world;

	public World getWorld() {
		return m_world;
	}

	public void setWorld(World world) {
		m_world = world;
	}

	public void moveToWorld(World world) {
		logger.debug("Moving player {} to world {}", this, world);
		if (m_world != null)
			m_world.removePlayer(this);
		assert(world != null);
		setWorld(world);
		m_world.addPlayer(this);
		m_world.getPolicy().apply(this);
		LevelGzipper.getLevelGzipper().gzipLevel(session);
	}
}
