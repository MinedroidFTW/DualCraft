package dualcraft.org.server.classic.extensions.brushes;

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

import dualcraft.org.server.classic.cmd.Command;
import dualcraft.org.server.classic.cmd.CommandParameters;
import dualcraft.org.server.classic.model.Player;

/**
 * 
 * 
 */

public final class BrushCommand extends Command {
	
	/**
	 * The instance of this command.
	 */
	private static final BrushCommand INSTANCE = new BrushCommand();
	
	/**
	 * Gets the singleton instance of this command.
	 * @return The singleton instance of this command.
	 */
	public static BrushCommand getBrushCommand() {
		return INSTANCE;
	}

	public String name() {
		return "brush";
	}
	
	/**
	 * Default private constructor.
	 */
	private BrushCommand() {
		/* empty */
	}
	
	private void usage(Player player) {
		player.getActionSender().sendChatMessage("/brush radius [radius]");
		player.getActionSender().sendChatMessage("/brush dim width height length");
		player.getActionSender().sendChatMessage("/brush dim [width|height|length] x");
		player.getActionSender().sendChatMessage("/brush [standard|default]");
		player.getActionSender().sendChatMessage("/brush delete [1|0]");
		player.getActionSender().sendChatMessage("/brush type [square|diamond|line|flat|box]");
	}
	
	public void execute(Player player, CommandParameters parameters) {
		
		String action = parameters.getStringArgument(0).toLowerCase();
		
		if (parameters.getArgumentCount() == 1) {
			if (action.equals("default") || action.equals("standard")) {
				player.setAttribute("brush", new StandardBrush());
				player.getActionSender().sendChatMessage("Now using standard brush");
			} else
				usage(player);
		}
		
		// If no brush then return
		if (player.getAttribute("brush") == null) {
			player.getActionSender().sendChatMessage("You don't have a brush!");
			player.getActionSender().sendChatMessage("Use: \"/brush standard\" to get one");
			return;
		}
		
		if (parameters.getArgumentCount() == 2) {
			
			if (action.equals("radius")) {
				try {
					int newRadius = parameters.getIntegerArgument(1);
					((Brush) player.getAttribute("brush")).setRadius(newRadius);
					player.getActionSender().sendChatMessage("Brush radius changed");
				} catch (Exception e) {
					player.getActionSender().sendChatMessage("/brush radius [radius]");
					return;
				}
				
			} else if (action.equals("delete")) {
				String onOff = parameters.getStringArgument(1);
				if (onOff.equals("1")) {
					((Brush) player.getAttribute("brush")).useForDelete(true);
					player.getActionSender().sendChatMessage("Using this brush to delete");
				} else if (onOff.equals("0")) {
					((Brush) player.getAttribute("brush")).useForDelete(false);
					player.getActionSender().sendChatMessage("Using standard brush to delete");
				} else
					player.getActionSender().sendChatMessage("/brush delete [1|0]");
			} else if (action.equals("type")) {
				String brush = parameters.getStringArgument(1).toLowerCase();
				int bRadius = ((Brush) player.getAttribute("brush")).getRadius();
				boolean delete = false;
				if (player.getAttribute("brush").getClass() != StandardBrush.class)
					delete = ((Brush) player.getAttribute("brush")).getUseForDelete();
				Brush newBrush;
				if (brush.equals("square"))
					newBrush = new SquareBrush();
				else if (brush.equals("diamond"))
					newBrush = new DiamondBrush();
				else if (brush.equals("line"))
					newBrush = new LineBrush();
				else if (brush.equals("flat"))
					newBrush = new FlatBrush();
				else if (brush.equals("box"))
					newBrush = new BoxBrush();
				else {
					player.getActionSender().sendChatMessage("/brush type [square|diamond|line|flat]");
					return;
				}
				newBrush.setRadius(bRadius);
				newBrush.useForDelete(delete);
				player.setAttribute("brush", newBrush);
				player.getActionSender().sendChatMessage("Brush type changed to " + brush);
			} else
				usage(player);
			
		} else if (parameters.getArgumentCount() == 3) {
			if (action.equals("dim")) {
				String axis = parameters.getStringArgument(1);
				int newSize;
				try {
					newSize = parameters.getIntegerArgument(2);
				} catch (Exception e) {
					player.getActionSender().sendChatMessage("Error: Could not read number");
					player.getActionSender().sendChatMessage("/brush dim [width|height|length] x");
					return;
				}
				Brush brush = (Brush) player.getAttribute("brush");
				if (axis.equals("length")) {
					brush.setLength(newSize);
				} else if (axis.equals("width")) {
					brush.setWidth(newSize);
				} else if (axis.equals("height")) {
					brush.setHeight(newSize);
				} else
					player.getActionSender().sendChatMessage("/brush dim [width|height|length] x");
			} else
				usage(player);
		} else if (parameters.getArgumentCount() == 4) {
			if (action.equals("dim")) {
				int w, h, l;
				try {
					w = parameters.getIntegerArgument(1);
					h = parameters.getIntegerArgument(2);
					l = parameters.getIntegerArgument(3);
				} catch (Exception e) {
					player.getActionSender().sendChatMessage("Error: Could not read numbers");
					player.getActionSender().sendChatMessage("/brush dim width height length");
					return;
				}
				Brush brush = (Brush) player.getAttribute("brush");
				brush.setWidth(w);
				brush.setHeight(h);
				brush.setLength(l);
			}
		} else
			usage(player);
	}
}
