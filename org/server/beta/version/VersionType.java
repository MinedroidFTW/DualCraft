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
package dualcraft.org.server.beta.version;

/**
 * A class that defines a single version type AND holds the values of each value type
 * 
 * 
 * @version 1.2.1.1
 */
public class VersionType implements Comparable {
    
    /**
     * The snapshot version type
     * 
     * @since 1.0.0.0
     */
    public final static VersionType SNAPSHOT = new VersionType(10, "Snapshot", "-SNAPSHOT", "A development snapshot that is likely unstable");
    
    /**
     * The alpha version type
     * 
     * @since 1.0.0.0
     */
    public final static VersionType ALPHA = new VersionType(20, "Alpha", "-ALPHA", "A very early release that probably has major bugs");
    
    /**
     * The beta version type
     * 
     * @since 1.0.0.0
     */
    public final static VersionType BETA = new VersionType(30, "Beta", "-BETA", "An early release that might have some bugs");
    
    /**
     * The release candidate version type
     * 
     * @since 1.0.0.0
     */
    public final static VersionType RELEASE_CANDIDATE = new VersionType(40, "Release Candidate", "-RC", "A slightly early release that is most likely stable");
    
    /**
     * The stable version type
     * 
     * @since 1.0.0.0
     */
    public final static VersionType STABLE = new VersionType(100, "Stable", "", "A stable release");
    
    /**
     * Gets an array of all the version types
     * 
     * @return The version types
     * @since 1.2.0.0
     */
    public static VersionType[] getVersionTypes() {
        return new VersionType[] {
            SNAPSHOT,
            ALPHA,
            BETA,
            RELEASE_CANDIDATE,
            STABLE
        };
    }
    
    /**
     * Gets a version type by name
     * 
     * @param name The name to look for
     * @return The version type, or STABLE if not found
     */
    public static VersionType getByName(final String name) {
        //Check each type
        for (VersionType type : getVersionTypes()) {
            //Does the name exist?
            if (type.getName().equalsIgnoreCase(name)) {
                return type; //Return the type
            }
        }
        
        //Can't find the version type, so use stable by default
        return VersionType.STABLE;
    }
    
    /**
     * Creates a new version type
     * 
     * @param order The order to use
     * @param name The name of this version type
     * @param postfix The postfix to use
     * @param description The description of this version type
     * @since 1.0.0.0
     */
    public VersionType(final int order, final String name, final String postfix, final String description) {
        //Set the order
        this.order = order;
        //Set the name
        this.name = name;
        //Set the postfix
        this.postfix = postfix;
        //Set the description
        this.description = description;
    }
    
    /**
     * The order of this version type
     * Used for sorting
     * 
     * @since 1.0.0.0
     */
    private int order;
    
    /**
     * The name of this version type
     * 
     * @since 1.0.0.0
     */
    private String name;
    
    /**
     * The postfix of this version type
     * 
     * @since 1.0.0.0
     */
    private String postfix;    
    
    /**
     * The description of this version type
     * 
     * @since 1.0.0.0
     */
    private String description;
    
    /**
     * Gets the order of this version type
     * 
     * @return The order
     * @since 1.0.0.0
     */
    public int getOrder() {
        return this.order;
    }
    
    /**
     * Gets the name of this version type
     * 
     * @return The name
     * @since 1.0.0.0
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the description of this version type
     * 
     * @return The description
     * @since 1.0.0.0
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Gets this version type's postfix
     * 
     * @return The postfix
     * @since 1.0.0.0
     */
    public String getPostfix() {
        return this.postfix;
    }
    
    /**
     * Sets this version type's postfix
     * 
     * @param postfix The postfix to use
     * @since 1.0.0.0
     */
    public void setPostfix(final String postfix) {
        this.postfix = postfix;
    }
    
    /**
     * Checks to see if this version type is stable
     * 
     * @return True if stable, otherwise false
     * @since 1.2.1.1
     */
    public boolean isStable() {
        return this.equals(VersionType.STABLE);
    }

    /**
     * Checks another version type to see if it's equal
     * 
     * @param otherClass The other class (nominally a VersionType)
     * @return True if the other class is equal
     * @since 1.2.1.0
     */
    @Override
    public boolean equals(final Object otherClass) {
        if (otherClass == null) {
            return false;
        }
        if (getClass() != otherClass.getClass()) {
            return false;
        }
        final VersionType other = (VersionType) otherClass;
        if (this.order != other.order) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.postfix == null) ? (other.postfix != null) : !this.postfix.equals(other.postfix)) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the hash code of this version type
     * 
     * @return The hash code
     * @since 1.2.1.0
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 10 * hash + this.order;
        hash = 15 * hash + (this.name == null ? 0 : this.name.hashCode());
        hash = 20 * hash + (this.postfix == null ? 0 : this.postfix.hashCode());
        hash = 25 * hash + (this.description == null ? 0 : this.description.hashCode());
        return hash;
    }

    /**
     * Compares this version type with another version type to see which one is higher in the order
     * 
     * @param other The other (version type)
     * @return 1 if the other type is smaller, 0 if they are the same, or -1 if the other type is larger
     * @since 1.1.0.0
     */
    @Override
    public int compareTo(final Object other) {
        //Check to see if the other object isn't a version type
        if (!(other instanceof VersionType)) {
            //Can't be cast
            throw new ClassCastException(other.getClass() + " cannot be cast to a VersionType!");
        }
        
        //Set up a new variable
        VersionType otherVersionType = (VersionType) other;
        
        //Check the order
        if (this.getOrder() > otherVersionType.getOrder()) {
            return 1;
        } else if (this.getOrder() < otherVersionType.getOrder()) {
            return -1;
        } else { 
            return 0;
        }
    }
    
}
