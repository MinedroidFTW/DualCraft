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
package dualcraft.org.server.beta.task;

/**
 * Defines a single runnable task
 * 
 * 
 * @version 1.1.0.0
 */
public abstract class AbstractRunnableTask implements Runnable {
        
    /**
     * Defines if this task uses a separate thread
     * 
     * @since 1.0.0.0
     */
    private boolean separateThread;
    
    /**
     * Checks to see if this task uses a separate thread
     * 
     * @return True if a separate thread is used
     * @since 1.0.0.0
     */
    public boolean usesSeparateThread() {
        return this.separateThread;
    }
    
    /**
     * Runs this task
     * 
     * @since 1.0.0.0
     */
    @Override    
    public void run() {
        //Execute the task
        this.execute();
        
        //See if the tread uses a new thread
        if (this.separateThread) {
            //Kill the used thread
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Executes this task
     * 
     * @since 1.0.0.0
     */
    protected abstract void execute();
    
    /**
     * The name of this task
     * 
     * @return The name
     * @since 1.0.0.0
     */
    public abstract String getName();
    
    /**
     * Registers, and runs, a new task
     * 
     * @param task The task to run
     * @param useSeparateThread Whether to use a new thread or not
     * @since 1.0.0.0
     */
    public static void register(AbstractRunnableTask task, boolean useSeparateThread) {
                
        //Check to see if the task doesn't use a new thread
        if (useSeparateThread) {
            //Ooh, a thread!
            task.separateThread = true;
            //Set the thread
            Thread thread = new Thread(task);
            //Set the name
            thread.setName(task.getName());
            //Start it
            thread.start();
        } else {
            task.separateThread = false;
            //Haha, I see what you did there. You got cluttered!
            task.execute();
        }
    }
           
}