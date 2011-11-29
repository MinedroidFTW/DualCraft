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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.model.Environment;
import dualcraft.org.server.classic.model.Position;
import dualcraft.org.server.classic.model.Rotation;
import dualcraft.org.server.classic.model.BlockConstants;

import org.slf4j.*;

public final class MCSharpFileHandler {
	private MCSharpFileHandler() {}

	private final static Logger logger = LoggerFactory.getLogger(MCSharpFileHandler.class);

	private static int convert(int s) {
		return (int)(((s>>8)&0xff)+((s << 8)&0xff00));
	}

	public static Level load(String filename) throws IOException {
		logger.trace("Loading {}", filename);
		Level lvl = new Level();
		FileInputStream in = new FileInputStream(filename);
		GZIPInputStream decompressor = new GZIPInputStream(in);

		DataInputStream data = new DataInputStream(decompressor);

		int magic = convert(data.readShort());
		logger.trace("Magic number: {}", magic);
		if (magic != 1874)
			throw new IOException("Only version 1 MCSharp levels supported (magic number was "+magic+")");

		int width = convert(data.readShort());
		int height = convert(data.readShort());
		int depth = convert(data.readShort());
		logger.trace("Width: {}", width);
		logger.trace("Depth: {}", depth);
		logger.trace("Height: {}", height);

		int spawnX = convert(data.readShort());
		int spawnY = convert(data.readShort());
		int spawnZ = convert(data.readShort());

		int spawnRotation = data.readUnsignedByte();
		int spawnPitch = data.readUnsignedByte();

		/*int visitRanks =*/ data.readUnsignedByte();
		/*int buildRanks =*/ data.readUnsignedByte();

		byte[][][] blocks = new byte[width][height][depth];
		for(int z = 0;z<depth;z++) {
			for(int y = 0;y<height;y++) {
				byte[] row = new byte[height];
				data.readFully(row);
				for(int x = 0;x<width;x++) {
					blocks[x][y][z] = translateBlock(row[x]);
				}
			}
		}

		lvl.setBlocks(blocks, new byte[width][height][depth], width, height, depth);
		lvl.setSpawnPosition(new Position(spawnX, spawnY, spawnZ));
		lvl.setSpawnRotation(new Rotation(spawnRotation, spawnPitch));
		lvl.setEnvironment(new Environment());

		return lvl;
	}

	public static byte translateBlock(byte b) {
		if (b <= 49)
			return b;
		if (b == 111)
			return BlockConstants.TREE_TRUNK;
		return BlockConstants.AIR;
	}
}
