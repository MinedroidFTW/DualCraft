package dualcraft.org.server.classic.io;

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

import java.io.IOException;
import java.io.ObjectStreamClass;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Environment;
import dualcraft.org.server.classic.model.Rotation;
import dualcraft.org.server.classic.model.Position;

/**
 * A level loader that loads up serialized levels.
 * 
 */
public final class MineFileHandler {

	/**
	 * Default private constructor.
	 */
	private MineFileHandler() { /* empty */ }
	
	/**
	 * Uses the magic of java introspection to load a level
	 * @param filename The name of the file to unzip
	 * @return The uncompressed Level
	 */
	public static Level load(String filename) throws IOException {
		Level lvl = new Level();
		FileInputStream in = new FileInputStream(filename);
		GZIPInputStream decompressor = new GZIPInputStream(in);
		DataInputStream data = new DataInputStream(decompressor);
		int magic = data.readInt();
		byte version = data.readByte();
		ObjectInputStream stream = new LevelDeserializer(decompressor);
		DeserializedLevel level;
		try {
			level = (DeserializedLevel)stream.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}

		if (level != null) {
			Environment env = new Environment();

			int width  = level.width;
			int height = level.height;
			int depth  = level.depth;
			byte[] fblocks = level.blocks;
			byte[][][] blocks = new byte[width][height][depth];

			int i = 0;
			for (int z = 0; z < depth; z++) {
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						blocks[x][y][z] = fblocks[i];
						i += 1;
					}
				}
			}

			lvl.setEnvironment(env);

			lvl.setBlocks(blocks, new byte[width][height][depth], width, height, depth);

			lvl.setSpawnPosition(new Position(level.xSpawn, level.ySpawn, level.zSpawn));
			lvl.setSpawnRotation(new Rotation((int)level.rotSpawn, 0));

			lvl.setName(level.name);
			lvl.setAuthor(level.creator);
			lvl.setCreationDate(level.createTime);
		} else {
			throw new IOException("Failed to load mine file");
		}
		

		return lvl;
	}

	public static void save(Level lvl, String filename) {
		//We'd need to basically steal code from minecraft to do this, since
		//there doesn't seem to be a way to make a ObjectStreamClass from
		//scratch with a custom class name.
		throw new UnsupportedOperationException();
	}
}
class DeserializedLevel implements Serializable {
	static final long serialVersionUID = 0L;
	public int cloudColor;
	public long createTime;
	public boolean creativeMode;
	public int depth;
	public int fogColor;
	public boolean growTrees;
	public int height;
	public boolean networkMode;
	public float rotSpawn;
	public int skyColor;
	public int tickCount;
	public int unprocessed;
	public int waterLevel;
	public int width;
	public int xSpawn;
	public int ySpawn;
	public int zSpawn;
	public Object blockMap;
	public byte[] blocks;
	public String creator;
	public String name;
	public Object player;
	public DeserializedLevel() {
	}

}
class LevelDeserializer extends ObjectInputStream {
	public LevelDeserializer(InputStream in) throws IOException {
		super(in);
	}

	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
		ObjectStreamClass desc = super.readClassDescriptor();
		if (desc.getName().equals("com.mojang.minecraft.level.Level")) {
			return ObjectStreamClass.lookup(DeserializedLevel.class);
		}
		return desc;
	}
}

