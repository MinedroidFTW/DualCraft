package dualcraft.org.server.classic.task;

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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.*;

/**
 * Manages the task queue.
 * 
 */
public final class TaskQueue {
	
	/**
	 * The task queue singleton.
	 */
	private static final TaskQueue INSTANCE = new TaskQueue();
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = LoggerFactory.getLogger(TaskQueue.class);
	
	/**
	 * Gets the task queue instance.
	 * @return The task queue instance.
	 */
	public static TaskQueue getTaskQueue() {
		return INSTANCE;
	}
	
	/**
	 * The scheduled executor service backing this task queue.
	 */
	private ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
	
	/**
	 * Default private constructor.
	 */
	private TaskQueue() {
		/* empty */
	}
	
	/**
	 * Pushes a task onto the task queue.
	 * @param task The task to be executed.
	 */
	public void push(final Task task) {
		service.submit(new Runnable() {
			public void run() {
				try {
					task.execute();
				} catch (Throwable t) {
					logger.error("Error during task execution.", t);
				}
			}
		});
	}
	
	/**
	 * Schedules a task to run in the future.
	 * @param task The scheduled task.
	 */
	public void schedule(final ScheduledTask task) {
		schedule(task, task.getDelay());
	}
	
	/**
	 * Internally schedules the task.
	 * @param task The task.
	 * @param delay The remaining delay.
	 */
	private void schedule(final ScheduledTask task, final long delay) {
		logger.trace("Scheduled task {} with delay {}", task, delay);
		service.schedule(new Runnable() {
			public void run() {
				long start = System.currentTimeMillis();
				try {
					task.execute();
				} catch (Throwable t) {
					logger.error("Error during task execution.", t);
				}
				if (!task.isRunning()) {
					return;
				}
				long elapsed = System.currentTimeMillis() - start;
				long waitFor = task.getDelay() - elapsed;
				if (waitFor < 0) {
					waitFor = 0;
				}
				schedule(task, waitFor);
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
	
}
