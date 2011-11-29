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
package dualcraft.org.server.beta.resource;

import dualcraft.org.server.beta.server.nbt.*;
import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A file resource handler that
 * 
 * @version 0.9.0.1
 */
public abstract class AbstractNBTResourceHandler extends AbstractFileResourceHandler<CompoundTag> {

    /**
     * The logger instance
     * 
     * @sicne 0.9.0.0
     */
    private static final Logger LOGGER = Logger.getLogger("NBTResourceHandler");
    
    /**
     * Whether GZip is used or not
     * 
     * @since 0.9.0.0
     */
    private final transient boolean useGZip;
    
    /**
     * Creates a new abstract NBT resource handler
     * 
     * @param file The file to use
     * @param useGZip True if the file is to be GZipped
     */
    public AbstractNBTResourceHandler(final File file, final boolean useGZip) {
        super(file);
        this.useGZip = useGZip;
    }
    
    /**
     * Loads a compound tag from the NBT stream
     * 
     * @return A compound tag
     * @since 0.9.0.0
     */
    @Override
    public CompoundTag load() {
        //Get a file input stream
        NBTInputStream stream = null;
        //Get the 
        try {
            //Set up the stream
            stream = new NBTInputStream(new FileInputStream(this.getFile()), this.useGZip);
            //Get the data
            AbstractTag data = stream.readTag();
            //And return the data
            return (CompoundTag) data;
        } catch (FileNotFoundException ex) {
            //OMG YOU IDIOT
            LOGGER.warning("Could not find " + this.getFile().getName() + ", loading default");
            //Save default values
            this.save(this.getDefaultResource());
            //I'm angry!
            return this.getDefaultResource();
        } catch (IllegalTagException ex) {
            //OMG YOU IDIOT
            LOGGER.log(Level.SEVERE, "Illegal tag found in " + this.getFile().getName(), ex);
            //I'm angry!
            return this.getDefaultResource();
        } catch (TagTypeNotFoundException ex) {
            //OMG YOU IDIOT
            LOGGER.log(Level.SEVERE, "Unknown tag found in " + this.getFile().getName(), ex);
            //I'm angry!
            return this.getDefaultResource();
        } catch (IOException ex) {
            //OMG YOU IDIOT
            LOGGER.warning("Error when loading " + this.getFile().getName() + ", loading default");
            //I'm angry!
            return this.getDefaultResource();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                LOGGER.severe("Could not close the stream for " + this.getFile().getName());
            }
        }
    }

    /**
     * Saves the compound tag
     * 
     * @param data The compound tag to save
     * @sicne 0.9.0.0
     */
    @Override
    public void save(final CompoundTag data) {
        
        NBTOutputStream stream = null;
        
        try {
            //Set up the stream
            stream = new NBTOutputStream(new FileOutputStream(this.getFile()), this.useGZip);
            //And write
            stream.writeTag(data);
        
        } catch (FileNotFoundException ex) {
            try {
                //OMG YOU IDIOT
                LOGGER.info("Could not find " + this.getFile().getName() + ", making a new file");
                //And make the file
                boolean madeDirectories = this.getFile().getParentFile().mkdirs();
                if (this.getFile().createNewFile()) {
                    this.save(data);
                } else {
                    LOGGER.severe("Could not make file " + this.getFile().getName());
                    LOGGER.fine("Made directories: " + (madeDirectories ? "YES" : "NO (May already exist)"));
                }
            } catch (IOException ex1) {
                //Um
                LOGGER.log(Level.SEVERE, "Something went wrong while making " + this.getFile().getName(), ex);
                
            }
        } catch (IOException ex) {
            //Um
            LOGGER.log(Level.SEVERE, "Something went wrong while saving to" + this.getFile().getName(), ex);
                        
        } catch (IllegalTagException ex) {
            
            LOGGER.log(Level.SEVERE, "Illegal tag found in " + this.getFile().getName(), ex);
            
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex) {
                LOGGER.severe("Could not close the stream for " + this.getFile().getName());
            }
        }
        
    }
    
}
