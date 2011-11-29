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
package dualcraft.org.server.beta.task.scheduled.impl.universal.generic;

import dualcraft.org.server.beta.task.impl.universal.generic.GCTask;
import dualcraft.org.server.beta.task.scheduled.AbstractScheduledTask;
import java.util.concurrent.TimeUnit;

/**
 * A scheduled task that runs the GC task
 * 
 * 
 * @version 1.0.0.0
 */
public class GCTaskRunner extends AbstractScheduledTask {

    /**
     * Gets this task's name
     * 
     * @return The name
     * @since 1.0.0.0
     */
    @Override
    public String getName() {
        return "GC Task Runner";
    }

    /**
     * Gets this task's delay
     * 
     * @return The delay
     * @since 1.0.0.0
     */
    @Override
    public int getDelay() {
        return 30;
    }

    /**
     * Gets this task's delay time unit
     * 
     * @return The delay time unit
     * @since 1.0.0.0
     */
    @Override
    public TimeUnit getDelayUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * Checks to see if this task repeats
     * 
     * @return True if repeating, otherwise false
     * @since 1.0.0.0
     */
    @Override
    public boolean repeats() {
        return true;
    }

    /**
     * Runs this task
     */
    @Override
    public void run() {
        GCTask.register(new GCTask(), true);
    }
    
}
