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
package dualcraft.org.server.classic.security;
import org.slf4j.*;

public class Permission {
	public static final Permission BUILD = new Permission("dualcraft.org.server.classic.Build");
	public static final Permission DESTROY = new Permission("dualcraft.org.server.classic.Destroy");
	public static final Permission DESTROY_OWN = new Permission("dualcraft.org.server.classic.DestroyOwn");

	private String m_name;
	public Permission(String name) {
		m_name = name;
	}

	private static final Logger logger = LoggerFactory.getLogger(Permission.class);

	public boolean implies(Permission permission) {
		logger.trace("Testing {} against {}", this, permission);
		if (permission.m_name.equals(m_name)) {
			logger.trace("Exact match.");
			return true;
		}
		String[] otherName = permission.m_name.split("\\.");
		String[] name = m_name.split("\\.");
		int segment = 0;
		while(segment < otherName.length && segment < name.length) {
			logger.trace("Checking section {} against {}", name[segment], otherName[segment]);
			if (name[segment].equals('*')) {
				logger.trace("Found wildcard.");
				return true;
			}
			if (!name[segment].equals(otherName[segment])) {
				logger.trace("Found discrepency.");
				return false;
			}
			segment++;
		}
		if (name.length != otherName.length) {
			logger.trace("Ran out of tokens.");
			return false;
		}
		logger.trace("Found a match.");
		return true;
	}

	public String toString() {
		return m_name;
	}
}
