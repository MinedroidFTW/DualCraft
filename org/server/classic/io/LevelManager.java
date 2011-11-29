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
import java.util.ArrayList;
import dualcraft.org.server.classic.model.Level;
import dualcraft.org.server.classic.Configuration;
import java.io.File;
import java.util.Arrays;
import org.slf4j.*;

/**
 * A Class to handle loading and saving of Levels
 * 
 */
public final class LevelManager {

	private static final Logger logger = LoggerFactory.getLogger(LevelManager.class);

	/**
	 * Default private constructor.
	 */
	private LevelManager() { /* empty */ }

	private static class Version implements Comparable<Version> {
		public File file;
		public int version;
		public String extension;
		public String name;
		public Version(File f, String mapName) {
			name = mapName;
			file = f;
			String baseName = f.getName().replace(mapName+"-", "");
			String[] tokens = baseName.split("\\.");
			try {
				version = Integer.parseInt(tokens[0]);
			} catch (NumberFormatException e) {
				logger.info("Invalid file name {}.", f);
				version = -1;
			}
			extension = tokens[1];
		}

		public int compareTo(Version other) {
			if (version == other.version)
				return 0;
			return version > other.version ? 1 : -1;
		}
	}

	public static Version[] getAllVersions(String mapName) {
		ArrayList<Version> list = new ArrayList<Version>();
		File base = new File("data/maps/"+mapName+"/");
		if (base.exists()) {
			for(File f : base.listFiles()) {
				list.add(new Version(f, mapName));
			}
			return list.toArray(new Version[list.size()]);
		}
		return new Version[0];
	}

	public static Version getLatestVersion(String mapName) {
		Version highest = null;
		for(Version v : getAllVersions(mapName)) {
			if (highest == null || v.version > highest.version)
				highest = v;
		}
		return highest;
	}
	
	public static String getExtension(String mapName, int ver) {
		for(Version v : getAllVersions(mapName)) {
			if (v.version == ver)
				return v.extension;
		}
		return "mclevel";
	}

	private static File getNextFile(String mapName) {
		File base = new File("data/maps/"+mapName);
		if (!base.exists())
			base.mkdirs();
		Version prev = getLatestVersion(mapName);
		if (prev == null)
			return new File("data/maps/"+mapName+"/"+mapName+"-0.mclevel");
		File next = new File("data/maps/"+mapName+"/"+mapName+"-"+(getLatestVersion(mapName).version+1)+".mclevel");
		if (!prev.file.exists())
			return prev.file;
		long then = prev.file.lastModified();
		long now = System.currentTimeMillis();
		long diff = (now-then)/60000;
		logger.trace("Time delta is {} minutes", diff);

		if (diff > Configuration.getConfiguration().getBackupPeriod())
			return next;
		return prev.file;
	}

	/**
	 * Determines the appropriate file type and loads it.
	 * @param filename The name of the file to unzip
	 * @return The Level
	 */
	public static Level load(String mapName) throws IOException {
		Version latest = getLatestVersion(mapName);
		if (latest != null) {
			File mapFile = getLatestVersion(mapName).file;
			String ext = mapFile.getName().substring(mapFile.getName().lastIndexOf(".") + 1);

			try {
				if (ext.equalsIgnoreCase("mclevel")) {
					return NBTFileHandler.load(mapFile.getPath());
				} else if (ext.equalsIgnoreCase("dat") || ext.equalsIgnoreCase("mine")) {
					return MineFileHandler.load(mapFile.getPath());
				} else if (ext.equalsIgnoreCase("lvl")) {
					return MCSharpFileHandler.load(mapFile.getPath());
				}
				logger.info("Unknown file extension {}. Trying all known formats.", ext);
			} catch (IOException e) {
				logger.info("File is of different type than expected. Trying all known formats.");
			}

			try {
				logger.info("Trying NBT .mclevel");
				return NBTFileHandler.load(mapFile.getPath());
			} catch (IOException e) { }

			try {
				logger.info("Trying old .mine/.dat");
				return MineFileHandler.load(mapFile.getPath());
			} catch (IOException e) { }

			try {
				logger.info("Trying MCSharp .lvl");
				return MCSharpFileHandler.load(mapFile.getPath());
			} catch (IOException e) { }
		}

		/*
		logger.info("Generating level instead of loading.");
		Level lvl = new Level();
		lvl.generateLevel();
		lvl.setName(mapName);
		return lvl;
		*/
		logger.warn("Cannot load level. No handlers found.");
		return null;
	}

	/**
	 * Determines the appropriate file type and saves the Level.
	 * @param filename The name of the file to unzip
	 * @return The Level
	 */
	public static void save(Level lvl) {
		File nextFile = getNextFile(lvl.getName());
		File temp = new File("data/maps/"+lvl.getName()+"/"+lvl.getName()+".tmp");
		NBTFileHandler.save(lvl, temp.getPath());
		assert(temp.exists());
		logger.trace("Renaming {} to {}", temp, nextFile);
		if (temp.renameTo(nextFile))
			logger.trace("Save complete");
		else
			logger.warn("Could not save to {}!", nextFile);
		int verCount = Configuration.getConfiguration().getBackupCount();
		logger.debug("Removing old versions to trim to {}", verCount);
		Version[] versions = getAllVersions(lvl.getName());
		if (versions.length <= verCount) {
			logger.debug("Not removing old versions, there's only {}", versions.length);
			return;
		}
		Arrays.sort(versions);
		for(int i = 0;i<versions.length && versions.length-i> verCount;i++) {
			logger.trace("Deleting {}", versions[i].file);
			versions[i].file.delete();
		}
	}
}
