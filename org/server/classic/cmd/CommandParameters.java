package dualcraft.org.server.classic.cmd;

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

/**
 * An immutable class which holds and parses the parameters passed to a
 * commmand.
 * 
 */
public class CommandParameters {
	
	/**
	 * Arguments array.
	 */
	private final String[] args;
	
	/**
	 * Creates the command parameters class.
	 * @param args The arguments of the command.
	 */
	public CommandParameters(String[] args) {
		this.args = (String[])(args.clone());
	}
	
	/**
	 * Gets the number of arguments.
	 * @return The number of arguments.
	 */
	public int getArgumentCount() {
		return args.length;
	}
	
	/**
	 * Gets the string argument at <code>pos</code>.
	 * @param pos The index in the array.
	 * @return The argument.
	 */
	public String getStringArgument(int pos) {
		return args[pos];
	}
	
	/**
	 * Gets the double argument at <code>pos</code>.
	 * @param pos The index in the array.
	 * @return The argument.
	 * @throws NumberFormatException if the argument is not a double.
	 */
	public double getDoubleArgument(int pos) {
		return Double.valueOf(args[pos]);
	}
	
	/**
	 * Gets the integer argument at <code>pos</code>.
	 * @param pos The index in the array.
	 * @return The argument.
	 * @throws NumberFormatException if the argument is not an integer.
	 */
	public int getIntegerArgument(int pos) {
		return Integer.valueOf(args[pos]);
	}
	
}
