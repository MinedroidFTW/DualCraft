package dualcraft.org.server.classic.model;

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

/**
 * Represents an individual block type.
 * 
 */
public class BlockDefinition {
	
	/**
	 * A method that exists for legacy reasons.
	 * @param id The block id.
	 * @return The block definition.
	 */
	public static BlockDefinition forId(int id) {
		return BlockManager.getBlockManager().getBlock(id);
	}
	
	/**
	 * The block name.
	 */
	private String name;
	
	/**
	 * The block ID.
	 */
	private int bid;
	
	/**
	 * The block's solidity.
	 */
	private boolean solid;
	
	/**
	 * The block's fluidity.
	 */
	private boolean liquid;
	
	/**
	 * The block's transparency.
	 */
	private boolean blocksLight;
	
	/**
	 * The block's size.
	 */
	private boolean halfBlock;
	
	/**
	 * The block's "full" version if it is a halfblock.
	 */
	private int fullCounterpart;
	
	/**
	 * The block's behaviour, as a string.
	 */
	private String behaviourName;
	
	/**
	 * The block's behaviour.
	 */
	private transient BlockBehaviour behaviour;
	
	/**
	 * The block's periodic physics check state.
	 */
	private boolean doesThink;
	
	/**
	 * Whether this block is a plant (whether it cares about the blocks above
	 * it)
	 */
	private boolean isPlant;
	
	/**
	 * The timer, in milliseconds, on which this block thinks.
	 */
	private long thinkTimer;

	private boolean gravity;

	private int strength;
	
	/**
	 * Constructor.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private BlockDefinition(String name, int bid, boolean solid, boolean liquid, boolean blocksLight, boolean halfBlock, boolean doesThink, boolean isPlant, long thinkTimer, int fullCounterpart, String behaviourName, boolean gravity, int strength) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		this.name = name;
		this.bid = bid;
		this.solid = solid;
		this.liquid = liquid;
		this.blocksLight = blocksLight;
		this.halfBlock = halfBlock;
		this.doesThink = doesThink;
		this.isPlant = isPlant;
		this.thinkTimer = thinkTimer;
		this.fullCounterpart = fullCounterpart;
		this.behaviourName = behaviourName.trim();
		this.gravity = gravity;
		this.strength = strength;
		if (behaviourName.length() > 0) {
			this.behaviour = (BlockBehaviour) Class.forName(this.behaviourName).newInstance();
		}
	}
	
	/**
	 * Resolves this object.
	 * @return A resolved object.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private Object readResolve() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return new BlockDefinition(name, bid, solid, liquid, blocksLight, halfBlock, doesThink, isPlant, thinkTimer, fullCounterpart, behaviourName, gravity, strength);
	}
	
	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the ID.
	 * @return The ID.
	 */
	public int getId() {
		return bid;
	}
	
	/**
	 * Gets the solidity.
	 * @return The solidity.
	 */
	public boolean isSolid() {
		return solid;
	}

	public boolean hasGravity() {
		return gravity;
	}

	public int getStrength() {
		return strength;
	}
	
	/**
	 * Gets the fluidity.
	 * @return The fluidity.
	 */
	public boolean isLiquid() {
		return liquid;
	}
	
	/**
	 * Gets the transparency.
	 * @return The transparency.
	 */
	public boolean doesBlockLight() {
		return blocksLight;
	}
	
	/**
	 * Gets the size.
	 * @return The size.
	 */
	public boolean isHalfBlock() {
		return halfBlock;
	}
	
	/**
	 * Gets the periodic physics check state.
	 * @return The... yeah.
	 */
	public boolean doesThink() {
		return doesThink;
	}
	
	/**
	 * Apply passive physics.
	 * @param level The level.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	public void behavePassive(Level level, int x, int y, int z) {
		if (behaviour == null) {
			return;
		}
		this.behaviour.handlePassive(level, x, y, z, this.bid);
	}
	
	/**
	 * Apply physics on block destruction.
	 * @param level The level.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	public void behaveDestruct(Level level, int x, int y, int z) {
		if (behaviour == null) {
			return;
		}
		this.behaviour.handleDestroy(level, x, y, z, this.bid);
	}
	
	/**
	 * Apply periodic physics.
	 * @param level The level.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	public void behaveSchedule(Level level, int x, int y, int z) {
		if (behaviour == null) {
			return;
		}
		this.behaviour.handleScheduledBehaviour(level, x, y, z, this.bid);
	}
	
	/**
	 * Gets the speed at which this block "thinks."
	 * @return The think speed.
	 */
	public long getTimer() {
		return thinkTimer;
	}
	
	/**
	 * Gets this blocks' "plant" state.
	 * @return Whether or not this block is a plant.
	 */
	public boolean isPlant() {
		return isPlant;
	}
	
	/**
	 * Gets the fullsize counterpart for this block.
	 * @return The fullsize counterpart ID.
	 */
	public int getFullCounterpart() {
		if (halfBlock) {
			return fullCounterpart;
		} else {
			return 0;
		}
	}
}
