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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A formatter that produces output on one line
 * 
 * 
 * @version 1.0.0.0
 */
public final class SingleLineFormatter extends Formatter {
        
    /**
     * The line separator on this system
     * 
     * @since 1.0.0.0
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Formats a log record
     * 
     * @param record The log record to format
     * @return The formatted record
     * @since 1.0.0.0
     */
    @Override
    public synchronized String format(final LogRecord record) {
                
        //Set up a string builder
        final StringBuilder builder = new StringBuilder(record.getMessage().length() + 25);
        
        //Start appending data
        //Add the formatted date
        builder.append(new SimpleDateFormat("h:mm:ss.SSSS a", Locale.getDefault()).format(new Date(record.getMillis())));
        //Add the severity
        builder.append(": [").append(record.getLevel().getLocalizedName()).append("] ");
        //And the prefix
        builder.append(record.getLoggerName()).append(" - ");
        //And finally the data
        builder.append(record.getMessage());
        //Oh, and we need to stop the next line being the same
        builder.append(LINE_SEPARATOR);
        
        //Return the output
        return builder.toString();
    }
    
}
