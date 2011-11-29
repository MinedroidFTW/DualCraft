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

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * Defines a single Yaml resource handler
 * 
 * 
 * @version 1.0.0.3
 * @param <T> The resource type to use
 * @param <Y> The raw format to use
 */
public abstract class AbstractYamlResourceHandler<T, Y> extends AbstractFileResourceHandler<T> {

    /**
     * Creates a new Yaml resource handler
     * 
     * @param file The file to use
     * @since 1.0.0.0
     */
    public AbstractYamlResourceHandler(final File file) {
        super(file);
    }
    
    /**
     * The logger for this class
     * 
     * @since 1.0.0.0
     */
    final static private Logger LOGGER = Logger.getLogger("YamlResourceHandler");

    /**
     * Loads the resource
     * 
     * @return The resource
     * @since 1.0.0.0
     */
    @Override
    public T load() {
        //Gets the YAML loader
        final Yaml rawYaml = new Yaml();
        //Get a file input stream
        FileInputStream stream = null;
        try {
            //Set up the stream
            stream = new FileInputStream(this.getFile());
            //Load the raw data
            final Y raw = (Y) rawYaml.load(stream);
            //Convert the data
            final T data = this.toRealData(raw);
            //And return the data
            return data;
        } catch (FileNotFoundException ex) {
            //OMG YOU IDIOT
            LOGGER.warning("Could not find " + this.getFile().getName() + ", loading default");
            //Save default values
            this.save(this.getDefaultResource());
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
     * Saves the resource
     * 
     * @param data The resource to save
     * @since 1.0.0.0
     */
    @Override
    public void save(final T data) {
        //Gets the YAML loader
        final Yaml rawYaml = new Yaml();
        //And a file writer
        FileWriter writer = null;
        try {
            //Set up the file writer
            writer = new FileWriter(this.getFile());
            //Convert the data
            final Y raw = this.toRawData(data);
            //And save it
            rawYaml.dump(raw, writer);
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
                        
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                LOGGER.severe("Could not close the writer for " + this.getFile().getName());
            }
        }
    }

    /**
     * Converts the T (real data) to Y (raw data)
     * 
     * @param realData The real data
     * @return The raw data (usually ArrayList<String> or HashMap<Object, String>
     * @since 1.0.0.0
     */
    public abstract Y toRawData(T realData);

    /**
     * Converts the Y (raw data) to T (real data) 
     * 
     * @param rawData The raw data (usually ArrayList<String> or HashMap<Object, String>
     * @return The real data
     * @since 1.0.0.0
     */
    public abstract T toRealData(Y rawData);
    
}
