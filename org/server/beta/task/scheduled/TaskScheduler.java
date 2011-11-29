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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Defines a single task scheduler
 * 
 * 
 * @version 1.0.0.2
 */
public class TaskScheduler {
    
    /**
     * The scheduled executor service
     * 
     * @since 1.0.0.0
     */
    private final ScheduledExecutorService service;
    
    /**
     * Gets the service
     * 
     * @return The service
     * @since 1.0.0.0
     */
    public ScheduledExecutorService getService() {
        return this.service;
    }
    
    /**
     * Creates a new task scheduler
     * 
     * @param minimumThreads The minimum amount of threads to have running at all times
     * @since 1.0.0.0
     */
    public TaskScheduler(int minimumThreads) {
        this.service = Executors.newScheduledThreadPool(minimumThreads);
    }
    
    /**
     * Schedules a new task
     * 
     * @param task The task to schedule
     * @since 1.0.0.0
     */
    public void schedule(AbstractScheduledTask task) {
        
        //Check to see if the task repeats
        if (task.repeats()) {
            //Repeating tasks start NOW
            this.getService().scheduleAtFixedRate(task, 0, task.getDelay(), task.getDelayUnit());
        } else {
            //And non-repeating may start with a delay if they want
            this.getService().schedule(task, task.getDelay(), task.getDelayUnit());
        }
    }
    
}
