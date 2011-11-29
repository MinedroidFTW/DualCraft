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

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.net.MinecraftSession;
import org.slf4j.*;

/**
 * A utility class for gzipping levels.
 * 
 */
public final class LevelGzipper {
	
	/**
	 * The singleton instance.
	 */
	private static final LevelGzipper INSTANCE = new LevelGzipper();
	
	/**
	 * Gets the level gzipper.
	 * @return The level gzipper.
	 */
	public static LevelGzipper getLevelGzipper() {
		return INSTANCE;
	}
	
	/**
	 * The executor service.
	 */
	private ExecutorService service = Executors.newCachedThreadPool();
	
	/**
	 * Default private constructor.
	 */
	private LevelGzipper() {
		/* empty */
	}

	private static final Logger logger = LoggerFactory.getLogger(LevelGzipper.class);

	private class ChunkOutputStream extends OutputStream {
		private MinecraftSession m_session;
		private byte[] m_chunk;
		private boolean m_closed = false;
		private int m_blocksSent = 0;
		private int m_blockCount;
		public ChunkOutputStream(MinecraftSession session, int blockCount) {
			m_session = session;
			m_blockCount = blockCount;
			m_chunk = new byte[0];
		}

		public void close() {
			logger.trace("Closing gzip output");
			m_closed = true;
			flush();
		}

		public void flush() {
			if (m_chunk.length == 1024 || m_closed) {
				logger.trace("{} bytes in buffer. Flushing for real.", m_chunk.length);
				int percent = (int) ((double)m_blocksSent/m_blockCount * 255D);
				m_blocksSent++;
				if (percent > 255)
					percent = 254;
				if (m_closed)
					percent = 255;
				m_session.getActionSender().sendLevelBlock(m_chunk.length, m_chunk, percent);
				m_chunk = new byte[0];
				m_session.getActionSender().sendLevelFinish();
				logger.trace("Chunk {}/{} sent.", m_blocksSent, m_blockCount);
			}
		}

		public void write(int b) {
			byte[] newChunk = new byte[m_chunk.length+1];
			newChunk[m_chunk.length] = (byte)b;
			m_chunk = newChunk;
			flush();
		}
	}

	/**
	 * Gzips and sends the level for the specified session.
	 * @param session The session.
	 */
	public void gzipLevel(final MinecraftSession session) {
		logger.debug("Gzipping world to {}", session);
		assert(session!=null);
		assert(session.getPlayer()!=null);
		assert(session.getPlayer().getWorld() != null);
		Level level = session.getPlayer().getWorld().getLevel();
		final int width = level.getWidth();
		final int height = level.getHeight();
		final int depth = level.getDepth();
		final byte[][][] blockData = (byte[][][])(session.getPlayer().getWorld().getLevel().getBlocks().clone());
		session.getActionSender().sendLevelInit();
		/*service.submit(new Runnable() {
			public void run() {
				try {
					ChunkOutputStream clientMap = new ChunkOutputStream(session, width*height*depth);
					logger.trace("Gzipping world");
					DataOutputStream dataOut = new DataOutputStream(new GZIPOutputStream(clientMap));
					logger.trace("Writing size");
					dataOut.writeInt(width*height*depth);
					logger.trace("Writing blocks");
					for(int z = 0;z<depth;z++) {
						for (int y = 0;y<height;y++) {
							for(int x = 0;x<width;x++) {
								dataOut.write(blockData[x][y][z]);
							}
						}
					}
					logger.trace("Closing map output");
					clientMap.close();
				} catch (IOException ex) {
					session.getActionSender().sendLoginFailure("Failed to gzip level. Please try again.");
					logger.warn("GZip failed.", ex);
				}
			}
		});*/
		service.submit(new Runnable() {
			public void run() {
				try {
					//TODO: Parallelize the compression and transmission
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					int size = width * height * depth;
					logger.trace("Gzipping world");
					DataOutputStream os = new DataOutputStream(new GZIPOutputStream(out));
					os.writeInt(size);
					for (int z = 0; z < depth; z++) {
						for (int y = 0; y < height; y++) {
							for (int x = 0; x < width; x++) {
								os.write(blockData[x][y][z]);
							}
						}
					}
					os.close();
					logger.trace("Gzip complete. Transmitting to client");
					byte[] data = out.toByteArray();
					IoBuffer buf = IoBuffer.allocate(data.length);
					buf.put(data);
					buf.flip();
					while (buf.hasRemaining()) {
						int len = buf.remaining();
						if (len > 1024) {
							len = 1024;
						}
						byte[] chunk = new byte[len];
						buf.get(chunk);
						int percent = (int) ((double) buf.position() / (double) buf.limit() * 255D);
						session.getActionSender().sendLevelBlock(len, chunk, percent);
					}
					session.getActionSender().sendLevelFinish();
					logger.trace("Level sent!");
				} catch (IOException ex) {
					session.getActionSender().sendLoginFailure("Failed to gzip level. Please try again.");
				}
			}
		});
	}
}
