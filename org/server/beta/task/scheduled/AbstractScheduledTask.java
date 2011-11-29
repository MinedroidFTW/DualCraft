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

package dualcraft.org.server.beta.task.scheduled;

import java.util.concurrent.TimeUnit;

/**
 * Defines a single scheduled task
 * 
 * 
 * @version 1.0.0.0
 */
public abstract class AbstractScheduledTask implements Runnable {
        
    /**
     * Gets the name of this task
     * 
     * @return The name
     */
    public abstract String getName();
    
    /**
     * Gets the delay used in this task
     * 
     * @return The delay to use
     */
    public abstract int getDelay();

    /**
     * Gets the TimeUnit used as the delay
     * 
     * @return The time unit used
     */
    public abstract TimeUnit getDelayUnit();

    /**
     * Gets whether this task repeats or not
     * 
     * @return Whether this task repeats or not
     */
    public abstract boolean repeats();

    @Override
    /**
     * Runs this task
     */
    public abstract void run();
}
