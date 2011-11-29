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
package dualcraft.org.server.beta.server.model.entity.living.mob;

/**
 * An enumeration that defines a mob's aggression level
 *
 * 
 * @version 2.0.1.1
 */
public class AggressionLevel {
    
    /**
     * The scared aggression level. Used for NPCs that are running away
     * 
     * @since 2.0.0.0
     */
    public final static AggressionLevel SCARED = new AggressionLevel(0, "Scared", false, 0);
    
    /**
     * The timid aggression level. Used for mobs such as chickens
     * 
     * @since 2.0.0.0
     */
    public final static AggressionLevel TIMID = new AggressionLevel(1, "Timid", false, 0);
    
    /**
     * The slightly aggressive aggression level. Used for mobs such as wolves and giants
     * 
     * @since 2.0.0.0
     */
    public final static AggressionLevel SLIGHTLY_AGGRESSIVE = new AggressionLevel(5, "Slightly aggressive", true, 4);
    
    /**
     * The aggressive aggression level. Used for mobs such as creepers, zombies, and endermen
     * 
     * @since 2.0.0.0
     */
    public final static AggressionLevel AGGRESSIVE = new AggressionLevel(10, "Aggressive", true, 10);
    
    /**
     * Gets an array of aggression levels
     * 
     * @return The aggression levels
     * @since 2.0.1.0
     */
    public static AggressionLevel[] getAggressionLevels() {
        return new AggressionLevel[] {
            SCARED,
            TIMID,
            SLIGHTLY_AGGRESSIVE,
            AGGRESSIVE
        };
    }
    
    /**
     * Gets an aggression level by name
     * 
     * @param name The name to search
     * @return The aggression level or SLIGHTLY_AGGRESSIVE if not found
     * @since 2.0.1.0
     */
    public static AggressionLevel getByName(final String name) {
        //Get each level
        for (AggressionLevel level : getAggressionLevels()) {
            //See if the names match
            if (level.getName().equalsIgnoreCase(name)) {
                //Return the level
                return level;
            }
        }
        //Return SLIGHTLY_AGGRESSIVE by default
        return SLIGHTLY_AGGRESSIVE;
    }
    
    /**
     * Constructs a new aggression level
     * 
     * @param order The order to use
     * @param name The name to use
     * @param randomlyAttacks True if random attacks are allowed
     * @param attackRadius The attack radius
     * @since 2.0.0.0
     */
    public AggressionLevel(final int order, final String name, final boolean randomlyAttacks, final int attackRadius) {
        //Set the order
        this.order = order;
        //Set the name
        this.name = name;
        //Set the randomly attack boolean
        this._randomlyAttacks = randomlyAttacks;
        //Set the random attack radius
        this.attackRadius = attackRadius;
    }
    
    /**
     * The name of this aggression level
     * 
     * @since 2.0.0.0
     */
    private final String name;
    
    /**
     * Gets the name of this aggression level
     * 
     * @return The name
     * @since 2.0.0.0
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * The order (used for comparisons)
     * 
     * @since 2.0.0.0
     */
    private final int order;
    
    /**
     * Gets the order of this aggression level
     * 
     * @return The order
     * @since 2.0.0.0
     */
    public int getOrder() {
        return this.order;
    }
    
    /**
     * True if random attacks are enabled, otherwise false
     * 
     * @since 2.0.0.0
     */    
    private final boolean _randomlyAttacks;
    
    /**
     * Checks to see if random attacks are enabled
     * 
     * @return True if enabled, otherwise false
     * @since 2.0.0.0
     */
    public boolean randomlyAttacks() {
        return this._randomlyAttacks;
    }
    
    /**
     * The random attack radius
     * 
     * @since 2.0.0.0
     */
    private final int attackRadius;
    
    /**
     * Gets the random attack radius
     * 
     * @return The random attack radius
     * @since 2.0.0.0
     */
    public int getAttackRadius() {
        return this.attackRadius;
    }

}
