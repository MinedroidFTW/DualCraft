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

import dualcraft.org.server.beta.server.nbt.Taggable;
import dualcraft.org.server.beta.server.nbt.impl.CompoundTag;
import dualcraft.org.server.beta.server.nbt.impl.IntTag;
import dualcraft.org.server.beta.server.nbt.impl.StringTag;

/**
 * A class that defines a single version
 *
 * 
 * @version 1.1.1.1
 */
public class Version implements Comparable, Taggable<CompoundTag> {

    /**
     * Creates a new version
     *
     * @deprecated Use Version(major, minor, build, revision, type) instead!
     * @since 1.0.0.0
     */
    public Version() {
        this(1, 0, 0, 0, VersionType.STABLE);
    }

    /**
     * Creates a new version
     * 
     * @param major The major version number
     * @deprecated Use Version(major, minor, build, revision, type) instead!
     * @since 1.0.0.0
     */
    public Version(final int major) {
        this(major, 0, 0, 0, VersionType.STABLE);
    }

    /**
     * Creates a new version
     * 
     * @param major The major version number
     * @param minor The minor version number
     * @deprecated Use Version(major, minor, build, revision, type) instead!
     * @since 1.0.0.0
     */
    public Version(final int major, final int minor) {
        this(major, minor, 0, 0, VersionType.STABLE);
    }

    /**
     * Creates a new version
     * 
     * @param major The major version number
     * @param minor The minor version number
     * @param build The build number
     * @deprecated Use Version(major, minor, build, revision, type) instead!
     * @since 1.0.0.0
     */
    public Version(final int major, final int minor, final int build) {
        this(major, minor, build, 0, VersionType.STABLE);
    }

    /**
     * Creates a new version
     * 
     * @param major The major version number
     * @param minor The minor version number
     * @param build The build number
     * @param revision The revision number
     * @deprecated Use Version(major, minor, build, revision, type) instead!
     * @since 1.0.0.0
     */
    public Version(final int major, final int minor, final int build, final int revision) {
        this(major, minor, build, revision, VersionType.STABLE);
    }

    /**
     * Creates a new version
     * 
     * @param major The major version number
     * @param minor The minor version number
     * @param build The build number
     * @param revision The revision number
     * @param type The version type to use
     * @since 1.0.0.0
     */
    public Version(final int major, final int minor, final int build, final int revision, final VersionType type) {
        //Set the major version number
        this.major = major;
        //Set the minor version number
        this.minor = minor;
        //Set the build number
        this.build = build;
        //Set the revision number
        this.revision = revision;
        //Set the version type
        this.type = type;
    }
    
    /**
     * The major version number
     *
     * @since 1.0.0.0
     */
    private int major;

    /**
     * Gets the major version number
     * 
     * @return The major version number
     * @since 1.0.0.0
     */
    public int getMajor() {
        return this.major;
    }

    /**
     * Sets the major version number
     * 
     * @param major The major version number to use
     * @since 1.0.0.0
     */
    public void setMajor(final int major) {
        this.major = major;
    }
    
    /**
     * The minor version number
     *
     * @since 1.0.0.0
     */
    private int minor;

    /**
     * Gets the minor version number
     * 
     * @return The minor version number
     * @since 1.0.0.0
     */
    public int getMinor() {
        return this.minor;
    }

    /**
     * Sets the minor version number
     * 
     * @param minor The minor version number to use
     * @since 1.0.0.0
     */
    public void setMinor(final int minor) {
        this.minor = minor;
    }
    
    /**
     * The build number
     *
     * @since 1.0.0.0
     */
    private int build;

    /**
     * Gets the build number
     * 
     * @return The build number
     * @since 1.0.0.0
     */
    public int getBuild() {
        return this.build;
    }

    /**
     * Sets the build number
     * 
     * @param build The build number
     * @since 1.0.0.0
     */
    public void setBuild(final int build) {
        this.build = build;
    }
    
    /**
     * The revision number
     *
     * @since 1.0.0.0
     */
    private int revision;

    /**
     * Gets the revision number
     * 
     * @return The revision number
     * @since 1.0.0.0
     */
    public int getRevision() {
        return this.revision;
    }

    /**
     * Sets the revision number
     * 
     * @param revision The revision number
     * @since 1.0.0.0
     */
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    /**
     * The type of this version
     *
     * @since 1.0.0.0
     */
    private VersionType type;

    /**
     * Gets this version's type
     * 
     * @return The type
     * @since 1.0.0.0
     */
    public VersionType getType() {
        return this.type;
    }

    /**
     * Sets this version's type
     * 
     * @param type The type to use
     * @since 1.0.0.0
     */
    public void setType(final VersionType type) {
        this.type = type;
    }
    
    /**
     * Checks to see if this version is stable
     * 
     * @return True if stable, otherwise false
     * @since 1.1.1.1
     */
    public boolean isStable() {
        return this.getType().isStable();
    }

    /**
     * Checks this version against another class to see if they are equal
     * 
     * @param otherClass The other class (nominally a Version)
     * @return True if the classes are equal
     * @since 1.1.1.0
     */
    @Override
    public boolean equals(Object otherClass) {
        if (otherClass == null) {
            return false;
        }
        if (getClass() != otherClass.getClass()) {
            return false;
        }
        final Version other = (Version) otherClass;
        if (this.major != other.major) {
            return false;
        }
        if (this.minor != other.minor) {
            return false;
        }
        if (this.build != other.build) {
            return false;
        }
        if (this.revision != other.revision) {
            return false;
        }
        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    /**
     * Gets the hash code of this version
     * 
     * @return The hash code
     * @since 1.1.1.0
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 2 * hash + this.major;
        hash = 12 * hash + this.minor;
        hash = 22 * hash + this.build;
        hash = 32 * hash + this.revision;
        hash = 42 * hash + (this.type == null ? 0 : this.type.hashCode());
        return hash;
    }
    
    /**
     * Compares this version with another version to see which one is higher in the order
     * 
     * @param other The other (version)
     * @return 1 if this version is higher, 0 if they are the same, or -1 if the other version is higher
     * @since 1.0.0.0
     */
    @Override
    public int compareTo(final Object other) {
        //Check to see if the other object isn't a version
        if (!(other instanceof Version)) {
            //Can't be cast
            throw new ClassCastException(other.getClass() + " cannot be cast to a Version!");
        }

        //Set up a new variable
        Version otherVersion = (Version) other;

        //Check the major version
        if (otherVersion.getMajor() > this.getMajor()) {
            //It's larger, so return -1
            return -1;
        } else if (otherVersion.getMajor() < this.getMajor()) {
            //It's smaller, so return 1
            return 1;
        }

        //Check the minor version
        if (otherVersion.getMinor() > this.getMinor()) {
            //It's larger, so return -1
            return -1;
        } else if (otherVersion.getMinor() < this.getMinor()) {
            //It's smaller, so return 1
            return 1;
        }

        //Check the build number
        if (otherVersion.getBuild() > this.getBuild()) {
            //It's larger, so return -1
            return -1;
        } else if (otherVersion.getBuild() < this.getBuild()) {
            //It's smaller, so return 1
            return 1;
        }

        //Check the major version
        if (otherVersion.getRevision() > this.getRevision()) {
            //It's larger, so return -1
            return -1;
        } else if (otherVersion.getRevision() < this.getRevision()) {
            //It's smaller, so return 1
            return 1;
        }

        //Check the version type
        return this.getType().compareTo(otherVersion.getType());

    }

    /**
     * Get a string-based representation of this version type
     * 
     * @return A string
     * @since 1.0.0.1
     */
    @Override
    public String toString() {
        return this.major + "." + this.minor + "." + this.build + "." + this.revision + this.type.getPostfix();
    }

    /**
     * Loads this version from a tag
     * 
     * @param tag The compound tag being loaded from
     * @since 1.1.0.0
     */
    @Override
    public void loadFromTag(final CompoundTag tag) {
        //Check to see if the major component exists
        this.major = tag.getContents().containsKey("major")
                ? (Integer) (tag.getContents().get("major").getContents())
                : 1;
        
        //Check to see if the minor component exists
        this.minor = tag.getContents().containsKey("minor")
                ? (Integer) (tag.getContents().get("minor").getContents())
                : 0;
        
        //Check to see if the build component exists
        this.build = tag.getContents().containsKey("build")
                ? (Integer) (tag.getContents().get("build").getContents())
                : 0;
        
        //Check to see if the revision component exists
        this.revision = tag.getContents().containsKey("revision")
                ? (Integer) (tag.getContents().get("revision").getContents())
                : 0;
        
        //Check to see if the type exists
        this.type = tag.getContents().containsKey("type")
                ? VersionType.getByName((String) (tag.getContents().get("type").getContents()))
                : VersionType.STABLE;

    }

    /**
     * Saves this version to a tag
     * 
     * @return The compound tag to save to
     * @since 1.1.0.0
     */
    @Override
    public CompoundTag saveToTag() {
        //Set up the compound tag
        CompoundTag tag = new CompoundTag();
        
        //Add the major version
        tag.getContents().put("major", new IntTag("major", this.major));
        
        //Add the minor version
        tag.getContents().put("minor", new IntTag("minor", this.minor));
        
        //Add the build number
        tag.getContents().put("build", new IntTag("build", this.build));
        
        //Add the revision number
        tag.getContents().put("revision", new IntTag("revision", this.revision));
        
        //Add the version type
        tag.getContents().put("type", new StringTag("type", this.type.getName()));
        
        //And return the tag
        return tag;
    }
}
