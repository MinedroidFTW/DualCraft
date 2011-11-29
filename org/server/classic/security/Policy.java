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

import dualcraft.org.server.classic.model.World;
import java.text.ParseException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import org.slf4j.*;

public class Policy {

	private static final Logger logger = LoggerFactory.getLogger(Policy.class);

	public Policy() {

	}

	public Policy(String world, Reader r) throws ParseException, IOException {
		m_world = world;
		readFrom(r);
		if (logger.isTraceEnabled()) {
			logger.trace("Groups: ");
			for(Group g : m_groups.values()) {
				logger.trace("\t{}", g);
				for(Permission p : g.getPermissions()) {
					if (p instanceof Role)
						logger.trace("\t\t@{}", p);
					else
						logger.trace("\t\t{}", p);
				}
			}
			logger.trace("Roles: ");
			for(Role role : m_roles.values()) {
				logger.trace("\t{}", role);
				for(Permission p : role.getPermissions()) {
					logger.trace("\t\t{}", p);
				}
			}
			logger.trace("Players: ");
			for(String player : m_userPermissions.keySet()) {
				logger.trace("\t{}", player);
				for(Group group : m_userGroups.get(player)) {
					logger.trace("\tMember of {}", group);
				}
			}
		}
	}

	private String m_world = "";

	public Group getGroup(String group) {
		return m_groups.get(group);
	}

	public Group[] getGroups() {
		return m_groups.values().toArray(new Group[m_groups.size()]);
	}

	public void apply(Principal p) {
		logger.trace("Clearing policy on {}", p);
		p.clearPolicy();
		for(Group group : m_groups.values()) {
			group.removeMember(p);
		} if (m_userGroups.get(p.getName()) != null) {
			for(Group group : m_userGroups.get(p.getName())) {
				logger.trace("Adding {} to {}", p, group);
				group.addMember(p);
			}
		}
		if (m_userPermissions.get(p.getName()) != null) {
			for(Permission permission : m_userPermissions.get(p.getName())) {
				logger.trace("Granting {} to {}", permission, p);
				p.addPermission(permission);
			}
		}
	}

	private HashMap<String, ArrayList<Permission>> m_userPermissions;
	private HashMap<String, Role> m_roles;

	private HashMap<String, Group> m_groups;
	private HashMap<String, ArrayList<Group>> m_userGroups;

	public void readFrom(Reader r) throws ParseException, IOException {
		m_userPermissions = new HashMap<String, ArrayList<Permission>>();
		m_groups = new HashMap<String, Group>();
		m_groups.put("ALL", new Group("ALL") {
			public boolean hasMember(Principal p) {
				return true;
			}
		});
		m_groups.put("NONE", new Group("NONE") {
			public boolean hasMember(Principal p) {
				return false;
			}
		});
		m_userPermissions = new HashMap<String, ArrayList<Permission>>();
		m_userGroups = new HashMap<String, ArrayList<Group>>();
		m_roles = new HashMap<String, Role>();
		StreamTokenizer tokens = new StreamTokenizer(r);
		tokens.slashSlashComments(true);
		tokens.slashStarComments(true);
		tokens.ordinaryChar('{');
		tokens.ordinaryChar('}');
		tokens.wordChars('@', '@');
		tokens.wordChars('*', '*');
		HashMap<String, ArrayList<BlockList>> lists = new HashMap<String, ArrayList<BlockList>>();
		lists.put("ROLE", new ArrayList<BlockList>());
		lists.put("GROUP", new ArrayList<BlockList>());

		ArrayList<PermissionBlock> permissions = new ArrayList<PermissionBlock>();
		ArrayList<WorldBlock> worlds = new ArrayList<WorldBlock>();
		double version = 0;
		while(tokens.nextToken() != StreamTokenizer.TT_EOF) {
			String block = tokens.sval.toUpperCase();
			logger.trace("Read new block type {}", tokens.sval);
			if (block.equals("VERSION")) {
				if (tokens.nextToken() != StreamTokenizer.TT_NUMBER)
					throw new ParseException("Version must be a number", tokens.lineno());
				version = tokens.nval;
				logger.debug("Loading policy version {}", version);
			} else if (lists.containsKey(block)) {
				lists.get(block).add(new BlockList(tokens));
			} else if (block.equals("ALLOW")) {
				permissions.add(new AllowBlock(tokens));
			} else if (block.equals("DENY")) {
				permissions.add(new DenyBlock(tokens));
			} else if (block.equals("WORLD")) {
				WorldBlock world = new WorldBlock(tokens);
				if (world.getName().equals(m_world)) {
					for(PermissionBlock pblock : world.getPermissions()) {
						permissions.add(pblock);
					}
				}
			} else {
				throw new ParseException("Unhandled block: "+tokens.sval, tokens.lineno());
			}
		}

		for(BlockList roleBlock : lists.get("ROLE")) {
			String role = roleBlock.getName();
			m_roles.put(role, new Role(role));
		}

		for(BlockList roleBlock : lists.get("ROLE")) {
			String role = roleBlock.getName();
			for(String permission : roleBlock.getMembers()) {
				if (permission.charAt(0)=='@')
					m_roles.get(role).addPermission(m_roles.get(permission.substring(1)));
				else
					m_roles.get(role).addPermission(new Permission(permission));
			}
		}

		for(BlockList groupBlock : lists.get("GROUP")) {
			Group g = new Group(groupBlock.getName());
			m_groups.put(groupBlock.getName(), g);
		}
		
		for(BlockList groupBlock : lists.get("GROUP")) {
			Group g = m_groups.get(groupBlock.getName());
			for(String member : groupBlock.getMembers()) {
				if (member.charAt(0) == '@') {
					g.addMember(m_groups.get(member.substring(1)));
				} else {
					if (!m_userGroups.containsKey(member))
						m_userGroups.put(member, new ArrayList<Group>());
					m_userGroups.get(member).add(g);
				}
			}
		}

		for(PermissionBlock p : permissions) {
			String target = p.getTarget();
			String permission = p.getPermission();
			Permission perm;
			if (permission.charAt(0) == '@')
				perm = m_roles.get(permission.substring(1));
			else
				perm = new Permission(permission);
			if (target.charAt(0) == '@') {
				m_groups.get(target.substring(1)).addPermission(perm);
			} else {
				m_userPermissions.get(target).add(perm);
			}
		}
	}


	private class WorldBlock {
		public String getName() {
			return m_name;
		}
		private String m_name;

		public PermissionBlock[] getPermissions() {
			return m_permissions.toArray(new PermissionBlock[m_permissions.size()]);
		}

		ArrayList<PermissionBlock> m_permissions = new ArrayList<PermissionBlock>();

		public WorldBlock(StreamTokenizer tokens) throws ParseException, IOException {
			if (tokens.nextToken() != StreamTokenizer.TT_WORD)
				throw new ParseException("Expected world name", tokens.lineno());
			m_name = tokens.sval;
			if (tokens.nextToken() != '{')
				throw new ParseException("Expected token {", tokens.lineno());
			while(tokens.nextToken() != '}') {
				String block = tokens.sval.toUpperCase();
				if (block.equals("ALLOW")) {
					m_permissions.add(new AllowBlock(tokens));
				} else if (block.equals("DENY")) {
					m_permissions.add(new DenyBlock(tokens));
				} else {
					throw new ParseException("Unhandled grant in world: "+block, tokens.lineno());
				}
			}
		}
	}


	private abstract class PermissionBlock {
		private String m_target;
		private String m_permission;

		public String getTarget() {
			return m_target;
		}

		public String getPermission() {
			return m_permission;
		}

		public PermissionBlock(StreamTokenizer tokens) throws ParseException, IOException {
			if (tokens.nextToken() != StreamTokenizer.TT_WORD)
				throw new ParseException("Expected a permission.", tokens.lineno());
			m_permission = tokens.sval;
			/*if (tokens.nextToken() != StreamTokenizer.TT_WORD || tokens.sval.toLowerCase().equals("to"))
				throw new ParseException("Expected token 'to', got '"+tokens.sval+"'", tokens.lineno());*/
			tokens.nextToken();
			if (tokens.nextToken() != StreamTokenizer.TT_WORD)
				throw new ParseException("Expected role or player name, got "+tokens.sval, tokens.lineno());
			m_target = tokens.sval;
		}
	}

	private class AllowBlock extends PermissionBlock {
		public AllowBlock(StreamTokenizer tokens) throws ParseException, IOException {
			super(tokens);
		}
	}

	private class DenyBlock extends PermissionBlock {
		public DenyBlock(StreamTokenizer tokens) throws ParseException, IOException {
			super(tokens);
		}
	}

	private class BlockList {
		private ArrayList<String> m_members = new ArrayList<String>();

		public ArrayList<String> getMembers() {
			return m_members;
		}

		private String m_name;

		public String getName() {
			return m_name;
		}

		public BlockList(StreamTokenizer tokens) throws ParseException, IOException {
			if (tokens.nextToken() != StreamTokenizer.TT_WORD)
				throw new ParseException("Expected block name", tokens.lineno());
			m_name = tokens.sval;
			if (tokens.nextToken() != '{')
				throw new ParseException("Expected {, got "+tokens.sval, tokens.lineno());
			while(tokens.nextToken() != '}') {
				m_members.add(tokens.sval);
			}
		}
	}
}
