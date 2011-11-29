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
package dualcraft.org.server.beta.logger;

import dualcraft.org.server.beta.server.model.Universe;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import jline.console.ConsoleReader;

/**
 * A JLine compliant log handler
 * 
 * 
 * @version 1.0.0.0
 */
public class JLineLogHandler extends ConsoleHandler {

    /**
     * Flush, rinse, repeat
     * 
     * @since 1.0.0.0
     */
    @Override
    public synchronized void flush() {
        try {
            Universe.getInstance().getReader().print(String.valueOf(ConsoleReader.RESET_LINE));
            Universe.getInstance().getReader().flush();
            super.flush();
            try {
                Universe.getInstance().getReader().drawLine();
            } catch (Exception ex) {
                Universe.getInstance().getReader().getCursorBuffer().clear();
            }
            Universe.getInstance().getReader().flush();
        } catch (IOException ex) {
        }
    }
}
