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
package dualcraft.org.server.beta.resource.impl.core.universal;

import dualcraft.org.server.beta.resource.AbstractSimpleYamlResourceHandler;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The settings resource handler
 * 
 * 
 * @version 0.2.0.0
 */
public class SettingsResourceHandler extends AbstractSimpleYamlResourceHandler<Map<String, Object>> {

    /**
     * Creates a new settings resource handler
     * 
     * @since 0.1.0.0
     */
    public SettingsResourceHandler() {
        super(new File("data/settings.yml"));
    }   

    /**
     * Gets the default resources
     * 
     * @return The default resources
     * @since 0.1.0.0
     */
    @Override
    public Map<String, Object> getDefaultResource() {
        
        //The default resource
        Map<String, Object> defaultResource = new LinkedHashMap<String, Object>(7);
        
        //Add the server's name
        defaultResource.put("serverName", "dualcraft ");
        
        //And the MOTD
        defaultResource.put("MOTD", "Currently in development");
        
        //And the maximum slots allowed
        defaultResource.put("maximumSlots", 50);
        
        //Set up the ports
        defaultResource.put("ports", Arrays.asList(25565));
        
        //And username verification
        defaultResource.put("verifyUsernames", true);
        
        //And the default game mode
        defaultResource.put("defaultGameMode", "Survival");
        
        //And the default world name
        defaultResource.put("defaultWorldName", "earth");
        
        //Return the default resource
        return defaultResource;
        
    }
    
}
