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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dualcraft.org.server.classic.extensions.brushes.Brush;
import dualcraft.org.server.classic.extensions.brushes.BrushCommand;
import dualcraft.org.server.classic.extensions.brushes.StandardBrush;
import dualcraft.org.server.classic.game.GameModeAdapter;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Player;

/**
 * An experimental game mode. Useful for testing things. Currently logs players
 * in memory and greets them accordingly. Now also has the ability to use
 * brushes Has some of the official commands of which op and deop relies on
 * memory
 * 
 */

public class ExperimentalGameMode extends GameModeAdapter<Player> {
	
	/**
	 * A map of players who have connected.
	 */
	private Map<String, Date> visitors = new HashMap<String, Date>();
	
	/**
	 * Registers the experimental game mode commands.
	 */
	public ExperimentalGameMode() {
		registerCommand(BrushCommand.getBrushCommand());
	}
	
	/**
	 * Get default 1 size block brush
	 * @return A 1 size block brush
	 */
	private Brush getDefaultBrush() {
		return new StandardBrush();
	}
	
	@Override
	public void playerConnected(Player player) {
		super.playerConnected(player);
		String name = player.getName();
		// New player?
		if (!visitors.containsKey(name)) {
			player.getSession().getActionSender().sendChatMessage("Welcome " + name + ".");
		} else {
			// Welcome back.
			String lastConnectDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(visitors.get(name));
			player.getSession().getActionSender().sendChatMessage("Welcome back " + name + ".");
			player.getSession().getActionSender().sendChatMessage("You last connect was: " + lastConnectDate + ".");
			
		}
		// Remember connection time
		visitors.put(name, new Date());
		
		// Give them a brush, if they do not have one already
		if(!player.isAttributeSet("brush")) {
			player.setAttribute("brush", getDefaultBrush());
		}
	}
	
	@Override
	public void playerDisconnected(Player player) {
		// we persist this :) player.removeAttribute("brush");
	}
	
	@Override
	public void setBlock(Player player, Level level, int x, int y, int z, int mode, int type) {
		super.setBlock(player, level, x, y, z, mode, type);
		((Brush) player.getAttribute("brush")).paint(player, level, x, y, z, mode, type);
	}
	
	@Override
	public void broadcastChatMessage(Player player, String message) { // TODO:
		// rank
		// colors?
		player.getWorld().broadcast(player, player.getName() + ": " + message);
	}
	
}
