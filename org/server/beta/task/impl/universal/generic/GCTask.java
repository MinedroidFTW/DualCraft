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
package dualcraft.org.server.beta.task.impl.universal.generic;

import dualcraft.org.server.beta.task.AbstractRunnableTask;
import java.util.logging.Logger;

/**
 * A task that collects garbage
 * 
 * 
 * @version 1.0.0.1
 */
public class GCTask extends AbstractRunnableTask {

    /**
     * Executes this task
     * 
     * @since 1.0.0.0
     */
    @Override
    protected void execute() {
        //Get the memory before deletion
        long memoryBeforeDeletion = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        //Collect garbage
        Runtime.getRuntime().gc();
        //And get the memory after deletion
        long memoryAfterDeletion = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        //And log 
        Logger.getLogger(this.getName()).info("Garbage collected, " + 
                ((memoryBeforeDeletion / 1024 / 1024)) + "mb -> " + 
                ((memoryAfterDeletion / 1024 / 1024 )) + "mb");
    }

    /**
     * Gets this task's name
     * 
     * @return The name
     * @since 1.0.0.0
     */
    @Override
    public String getName() {
        return "GC Task";
    }
    
}
