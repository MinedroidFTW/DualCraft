package dualcraft.org.server.classic.security;

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

import java.util.ArrayList;
import org.slf4j.*;

public class Group implements Principal {
	private String m_name;

	private final static Logger logger = LoggerFactory.getLogger(Group.class);
	
	private ArrayList<Principal> m_members;

	public Group(String name) {
		m_name = name;
		m_members = new ArrayList<Principal>();
	}

	public boolean equals(Object another) {
		if (another instanceof Group) {
			Group grp = (Group)another;
			return m_name.equals(grp.m_name);
		}
		return false;
	}

	public String getName() {
		return m_name;
	}

	public int hashCode() {
		return m_name.hashCode();
	}

	public String toString() {
		return m_name;
	}

	public boolean isAuthorized(Permission perm) {
		for(Permission p : m_permissions) {
			logger.trace("Testing permission {} on {}", perm, p);
			if (p.implies(perm)) {
				logger.trace("Group {} can perform {}", this, perm);
				return true;
			}
		}
		logger.trace("Group {} can not perform {}", this, perm);
		return false;
	}

	private ArrayList<Permission> m_permissions = new ArrayList<Permission>();

	public Permission[] getPermissions() {
		return m_permissions.toArray(new Permission[m_permissions.size()]);
	}

	public void addPermission(Permission p) {
		m_permissions.add(p);
	}

	public boolean hasMember(Principal p) {
		return m_members.contains(p);
	}

	public void addMember(Principal p) {
		m_members.add(p);
	}

	public void clearPolicy() {
		m_members = new ArrayList<Principal>();
		m_permissions = new ArrayList<Permission>();
	}

	public void removeMember(Principal p) {
		m_members.remove(p);
	}

	public Principal[] getMembers() {
		return m_members.toArray(new Principal[m_members.size()]);
	}
}
