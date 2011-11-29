package dualcraft.org.server.classic.task;

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
 * Represents a task which can be repeated multiple times and then stopoped.
 * 
 */
public abstract class ScheduledTask implements Task {
	
	/**
	 * The delay.
	 */
	private long delay;
	
	/**
	 * Running flag.
	 */
	private boolean running = true;
	
	/**
	 * Creates a scheduled task with the specific delay.
	 * @param delay The delay.
	 */
	public ScheduledTask(long delay) {
		this.delay = delay;
	}
	
	/**
	 * Gets the delay.
	 * @return The delay.
	 */
	public long getDelay() {
		return delay;
	}
	
	/**
	 * Sets the delay.
	 * @param delay The delay.
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	/**
	 * Checks the is running flag.
	 * @return The is running flag.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Stops the server.
	 */
	public void stop() {
		running = false;
	}
	
}
