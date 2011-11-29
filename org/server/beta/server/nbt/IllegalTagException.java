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
package dualcraft.org.server.beta.server.nbt;

/**
 * An illegal tag exception
 *
 * 
 * @version 1.0.0.1
 */
public class IllegalTagException extends Exception {

    /**
     * Creates a new illegal tag exception
     * 
     * @param message The message
     * @param cause The cause
     * @since 1.0.0.0
     */
    public IllegalTagException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new illegal tag exception
     * 
     * @param cause The cause
     * @since 1.0.0.0
     */
    public IllegalTagException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new illegal tag exception
     * 
     * @param message The message
     * @since 1.0.0.0
     */
    public IllegalTagException(String message) {
        super(message);
    }

    /**
     * Creates a new illegal tag exception
     * 
     * @since 1.0.0.0
     */
    public IllegalTagException() {
        super();
    }
}
