package org.firstinspires.ftc.teamcode.dynamite;

import java.util.concurrent.atomic.AtomicBoolean;

// Made by Claude Sonnet 5
public class TimedLoopThread {
    private int threadPriotiry = Thread.NORM_PRIORITY;

    public double getUpdateRate() {
        return updateRate;
    }

    public interface ExitListener {
        void onLoopExit(Throwable cause);
    }

    private final double updateRate;
    private final Runnable task;
    private final long periodNanos;
    private final ExitListener exitListener;   // nullable

    private Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile boolean stopRequested = false;
    private volatile boolean sleeping = false;
    public TimedLoopThread(Runnable task, double frequencyHz) {
        this(task, frequencyHz, null);
    }

    public TimedLoopThread(Runnable task, double frequencyHz, ExitListener exitListener) {
        this.task = task;
        this.periodNanos = (long) (1_000_000_000.0 / frequencyHz);
        this.exitListener = exitListener;
        this.updateRate = frequencyHz;
    }

    private void runLoop() {
        Throwable failure = null;
        try {
            while (!stopRequested) {
                long startTime = System.nanoTime();

                try {
                    task.run();
                } catch (Throwable t) {
                    // Must not escape this thread. An uncaught throwable here reaches the VM
                    // default handler, which takes the whole Robot Controller down.
                    failure = t;
                    stopRequested = true;
                    break;
                }

                if (stopRequested) break;

                long elapsed = System.nanoTime() - startTime;
                long remaining = periodNanos - elapsed;

                if (remaining > 0) {
                    sleeping = true;
                    try {
                        long millis = remaining / 1_000_000;
                        int nanos = (int) (remaining % 1_000_000);
                        Thread.sleep(millis, nanos);
                    } catch (InterruptedException e) {
                        // woken up by stop() while sleeping - fall through, loop condition will exit
                    } finally {
                        sleeping = false;
                    }
                }
                // if remaining <= 0, task took longer than the period - skip sleep, loop again immediately
            }
        } catch (Throwable outer) {
            // anything thrown by the loop scaffolding itself rather than by the task
            failure = outer;
        } finally {
            sleeping = false;
            running.set(false);
            // Fires unconditionally. This is what lets the OpMode side distinguish
            // "stopped on purpose" from "died", instead of having to guess.
            if (exitListener != null) {
                try {
                    exitListener.onLoopExit(failure);
                } catch (Throwable ignored) {
                    // a throwing listener must not resurrect the crash
                }
            }
        }
    }

    /** Starts the loop thread. If already running, does nothing. */
    public synchronized void start() {
        if (running.get()) return;

        stopRequested = false;
        sleeping = false;
        running.set(true);
        thread = new Thread(this::runLoop, "TimedLoopThread");
        thread.setDaemon(true);
        // Backstop: replaces the VM default handler for this thread only, so even a
        // throwable that somehow escapes runLoop cannot kill the process.
        thread.setUncaughtExceptionHandler((th, t) -> {
            sleeping = false;
            running.set(false);
            if (exitListener != null) {
                try {
                    exitListener.onLoopExit(t);
                } catch (Throwable ignored) {
                }
            }
        });
        thread.setPriority(threadPriotiry);
        thread.start();
    }

    /** Requests the loop to stop and waits indefinitely for it to exit. */
    public void stop() {
        stop(0L);
    }

    /**
     * Requests the loop to stop, interrupting an in-progress inter-iteration sleep, then
     * waits up to timeoutMillis for the thread to exit (0 = wait forever).
     *
     * The join deliberately happens OUTSIDE the monitor. The old version held the lock
     * across an unbounded join(), so if one caller (say the DYN thread, via
     * PPInterface.stopFollowerUpdater) got stuck waiting on a wedged task, every other
     * caller - including the OpMode thread - blocked behind it on the monitor. A bounded
     * timeout is meaningless if you can be stalled acquiring the lock first.
     */
    public void stop(long timeoutMillis) {
        Thread t;
        synchronized (this) {
            if (!running.get()) return;
            stopRequested = true;
            t = thread;
            if (t != null && sleeping) t.interrupt();
        }

        if (t == null || t == Thread.currentThread()) return;

        try {
            t.join(timeoutMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** @return true if the loop thread is currently running */
    public boolean isRunning() {
        return running.get();
    }

    /** Exposed so the OpMode side can audit thread liveness. May be null before start(). */
    public Thread getThread() {
        return thread;
    }

    public void setPriority(int priority) {
        threadPriotiry = priority;
    }

    public void itterUpdate(){
        if (running.get()) stop();
        task.run();
    }
}