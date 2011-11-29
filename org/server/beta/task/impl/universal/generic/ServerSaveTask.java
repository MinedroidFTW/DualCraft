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

import dualcraft.org.server.beta.resource.impl.core.universal.SettingsResourceHandler;
import dualcraft.org.server.beta.server.model.Universe;
import dualcraft.org.server.beta.task.AbstractRunnableTask;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * A task that saves the entire server
 * 
 * 
 * @version 0.1.0.0
 */
public class ServerSaveTask extends AbstractRunnableTask {

    /**
     * Executes this task
     * 
     * @since 0.1.0.0
     */
    @Override
    protected void execute() {
        //Notify
        Logger.getLogger(this.getName()).info("Saving everything...");
        //Get the settings handler
        final SettingsResourceHandler settingsHandler = new SettingsResourceHandler();
        //And save it
        settingsHandler.save((LinkedHashMap<String, Object>) Universe.getInstance().getSettings());
        //Log the save status
        Logger.getLogger(this.getName()).info("Saved settings");
        
        //And notify
        Logger.getLogger(this.getName()).info("All data saved");
    }

    /**
     * Gets the name of this task
     * 
     * @return The name
     * @since 0.1.0.0
     */
    @Override
    public String getName() {
        return "Server save task";
    }
    
}
