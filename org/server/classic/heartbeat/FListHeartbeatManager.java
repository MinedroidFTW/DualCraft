package dualcraft.org.server.classic.heartbeat;

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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.*;

/**
 * A class which manages heartbeats.
 * 
 */
public class FListHeartbeatManager {
	
	/**
	 * The singleton instance.
	 */
	private static final FListHeartbeatManager INSTANCE = new FListHeartbeatManager();
	
	/**
	 * Heartbeat server URL.
	 */
	public static final URL URL;
	
	/**
	 * Initializes the heartbeat server URL.
	 */
	static {
		try {
			URL = new URL("http://list.fragmer.net/announce.php");
		} catch (MalformedURLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FListHeartbeatManager.class);
	
	/**
	 * Gets the heartbeat manager instance.
	 * @return The heartbeat manager instance.
	 */
	public static FListHeartbeatManager getHeartbeatManager() {
		return INSTANCE;
	}
	
	/**
	 * An executor service which executes HTTP requests.
	 */
	private ExecutorService service = Executors.newSingleThreadExecutor();
	
	/**
	 * Default private constructor.
	 */
	private FListHeartbeatManager() {
		/* empty */
	}
	
	/**
	 * Sends a heartbeat with the specified parameters. This method does not
	 * block.
	 * @param parameters The parameters.
	 */

	public void sendHeartbeat(final Map<String, String> parameters) {
		logger.debug("Enqueuing heartbeat");
		
		service.submit(new Runnable() {
			public void run() {
				logger.debug("Sending flist heartbeat");
				// assemble POST data
				StringBuilder bldr = new StringBuilder();
				for (Map.Entry<String, String> entry : parameters.entrySet()) {
					logger.trace("Handling key {}", entry.getKey());
					bldr.append(entry.getKey());
					bldr.append('=');
					try {
						bldr.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
						logger.trace("Added {} to parameters", entry.getKey());
					} catch (UnsupportedEncodingException e) {
						logger.error("Bad parameters for flist heartbeat", e);
						return;
					}
					bldr.append('&');
				}
				if (bldr.length() > 0) {
					bldr.deleteCharAt(bldr.length() - 1);
				}
				logger.trace("Sending {} to flist", bldr);
				// send it off
				try {
					HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
					byte[] bytes = bldr.toString().getBytes();
					conn.setDoOutput(true);
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
					conn.setUseCaches(false);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					logger.trace("Connecting");
					conn.connect();
					try {
						DataOutputStream os = new DataOutputStream(conn.getOutputStream());
						try {
							os.write(bytes);
						} finally {
							os.close();
						}
					} finally {
						conn.disconnect();
					}
					logger.info("Pinged FList");
					logger.trace("Error code: {}", conn.getResponseCode());
				} catch (IOException ex) {
					logger.warn("Error sending hearbeat.", ex);
				}
			}
		});
	}
}
