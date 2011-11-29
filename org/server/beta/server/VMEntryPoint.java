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
package dualcraft.org.server.beta.server;

import dualcraft.org.server.beta.logger.JLineLogHandler;
import dualcraft.org.server.beta.logger.SingleLineFormatter;
import dualcraft.org.server.beta.server.model.Universe;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * A class that holds the Virtual Machine entry point
 * 
 * 
 * @version 1.0.1.0
 */
public final class VMEntryPoint {
    
    /**
     * Not used.
     * 
     * @since 1.0.0.1
     */
    private VMEntryPoint() { }
    
    /**
     * The main method
     * 
     * @param arguments The arguments
     * @since 1.0.0.0
     */
    public static void main(final String[] arguments) {
        //Remove all of the default logger handlers
        for (Handler handler : Logger.getLogger("").getHandlers()) {
            //Remove the handler
            Logger.getLogger("").removeHandler(handler);
        }
        
        //Get the handler
        final Handler handler = new JLineLogHandler();
        
        //Set the format
        handler.setFormatter(new SingleLineFormatter());
        
        //And add the handler
        Logger.getLogger("").addHandler(handler);
        
        //Actually make the universe begin :P
        Universe.getInstance().begin();
    }
    
}
