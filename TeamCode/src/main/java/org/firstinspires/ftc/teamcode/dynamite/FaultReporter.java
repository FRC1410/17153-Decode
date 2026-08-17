package org.firstinspires.ftc.teamcode.dynamite;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

// written by Claude Opus 5
/**
 * Collects failures from non-OpMode threads (the DYN script thread, the follower update
 * thread) so they can be rethrown on the OpMode thread instead of killing the Robot
 * Controller process.
 *
 * The design goal is that a worker thread CANNOT die quietly. Three independent
 * mechanisms back that up:
 *
 *   1. WRITE-FIRST. report() dumps the complete stack trace - including the whole
 *      "Caused by" chain - to RobotLog and stderr synchronously, on the failing thread,
 *      before it touches the queue. If everything downstream fails, or the process dies
 *      a microsecond later, the trace is already in logcat and the RC log file.
 *
 *   2. NOTHING IS DROPPED. Faults go into an unbounded queue, not a first-wins slot.
 *      Every fault from every thread is delivered.
 *
 *   3. DEATH AUDIT. Every worker thread registers here and marks itself accounted-for
 *      when it exits. auditThread() runs on the OpMode thread and synthesises a fault for
 *      any thread found in state TERMINATED that never accounted for itself - which is
 *      exactly the "silently died" case that no catch block can observe.
 *
 * Delivery to the Driver Station is the last step, not the only one.
 */
final class FaultReporter {

    public static final String TAG = "DYN";

    /** Canonical thread names, so the same string is used at report and audit time. */
    public static final String DYN = "DYN";
    public static final String FOLLOWER = "FOLLOWER UPDATE";

    // ---------------------------------------------------------------- Fault

    public static final class Fault {
        public final String source;
        public final Throwable cause;
        /** Full trace text, cause chain included. Captured at report time. */
        public final String trace;
        public final long wallClockMs;

        Fault(String source, Throwable cause) {
            this.source = source;
            this.cause = cause;
            this.trace = render(cause);
            this.wallClockMs = System.currentTimeMillis();
        }

        public String header() {
            return "";
        }

        public String describe() {
            return "\n\n" + header() + "\n" + trace;
        }

        public String[] traceLines() {
            return trace.split("\n");
        }

        /** printStackTrace into a string: this is what preserves the Caused by chain. */
        private static String render(Throwable t) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        }
    }

    /**
     * What actually gets thrown on the OpMode thread. getMessage() carries the full text
     * of every pending fault, because the SDK surfaces the message - not the trace - on
     * the Driver Station.
     */
    public static final class WorkerThreadFault extends RuntimeException {
        public WorkerThreadFault(String fullText, Throwable firstCause) {
            super(fullText, firstCause);
        }
    }

    // ---------------------------------------------------------------- state

    private final ConcurrentLinkedQueue<Fault> pending = new ConcurrentLinkedQueue<>();
    private final Set<Thread> accountedFor = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private volatile int totalFaults = 0;
    private volatile boolean shuttingDown = false;

    /** Wipe state from a previous OpMode run. Call from init(). */
    public void reset() {
        pending.clear();
        accountedFor.clear();
        totalFaults = 0;
        shuttingDown = false;
    }

    /**
     * Marks that we are tearing threads down on purpose, so an InterruptedException raised
     * by our own interrupt() is classified as expected rather than as a crash. It is still
     * logged - just at warn level, and it is not queued for rethrow.
     */
    public void beginShutdown() {
        shuttingDown = true;
    }

    // ---------------------------------------------------------------- worker side

    /**
     * Called ON THE FAILING THREAD. Logs the full trace before doing anything else.
     * Never throws.
     */
    public void report(String source, Throwable t) {
        if (t == null) return;

        if (shuttingDown && isInterruption(t)) {
            RobotLog.ww(TAG, "%s thread interrupted during shutdown (expected): %s", source, t);
            markAccountedFor(Thread.currentThread());
            return;
        }

        Fault f = new Fault(source, t);
        totalFaults++;

        // 1. durable channels first - these must happen even if step 2 never does
        try {
            RobotLog.ee(TAG, t, "Uncaught exception on the %s thread", source);
        } catch (Throwable loggingFailed) {
            // RobotLog should never fail, but if it does we still have stderr below
        }
        System.err.println(f.describe());

        // 2. queue for delivery to the OpMode thread
        pending.add(f);
        markAccountedFor(Thread.currentThread());
    }

    /** Called from a worker's finally block: "I exited, and you know why." */
    public void noteExit(Thread t) {
        markAccountedFor(t);
    }

    public void noteExit(Thread t, String reason) {
        RobotLog.ii(TAG, "%s thread exited: %s", t == null ? "?" : t.getName(), reason);
        markAccountedFor(t);
    }

    private void markAccountedFor(Thread t) {
        if (t != null) accountedFor.add(t);
    }

    private static boolean isInterruption(Throwable t) {
        for (Throwable c = t; c != null; c = c.getCause()) {
            if (c instanceof InterruptedException) return true;
        }
        return Thread.currentThread().isInterrupted();
    }

    // ---------------------------------------------------------------- OpMode side

    /**
     * Mechanism 3. Call every loop. If the thread has terminated but never accounted for
     * itself, no catch block ever saw the failure - so manufacture one rather than let it
     * pass unnoticed.
     */
    public void auditThread(String source, Thread t) {
        if (t == null) return;
        if (t.getState() != Thread.State.TERMINATED) return;
        if (!accountedFor.add(t)) return;   // add() returns false if already present

        Throwable synthetic = new IllegalStateException(
                "The " + source + " thread terminated without completing normally and "
                        + "without reporting an exception. It died silently - check logcat "
                        + "around this timestamp for a VM-level error (OOM, ThreadDeath, "
                        + "linkage error).");
        totalFaults++;
        RobotLog.ee(TAG, synthetic, "Silent death of the %s thread", source);
        System.err.println(new Fault(source, synthetic).describe());
        pending.add(new Fault(source, synthetic));
    }

    public boolean hasPending() {
        return !pending.isEmpty();
    }

    public int getTotalFaults() {
        return totalFaults;
    }

    /** Removes and returns everything queued. */
    public List<Fault> drain() {
        List<Fault> out = new ArrayList<>();
        Fault f;
        while ((f = pending.poll()) != null) out.add(f);
        return out;
    }

    /** Builds the exception to throw on the OpMode thread. Returns null if list is empty. */
    public static WorkerThreadFault toThrowable(List<Fault> faults) {
        if (faults == null || faults.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        if (faults.size() > 1) {
            sb.append("\n\n").append(faults.size()).append(" WORKER THREAD FAULTS:");
        }
        for (Fault f : faults) sb.append(f.describe());
        return new WorkerThreadFault(sb.toString(), faults.get(0).cause);
    }

    /**
     * Last-resort drain for stop(): the OpMode is going away and nothing can be thrown any
     * more, so re-log everything undelivered at error level and push it into the SDK's
     * global error banner. This closes the "stopped before the next loop tick" hole.
     */
    public void flushUndelivered() {
        List<Fault> left = drain();
        if (left.isEmpty()) return;
        StringBuilder sb = new StringBuilder();
        for (Fault f : left) {
            RobotLog.ee(TAG, f.cause,
                    "UNDELIVERED %s fault (OpMode stopped before it could be rethrown)", f.source);
            System.err.println(f.describe());
            sb.append(f.describe());
        }
        try {
            RobotLog.setGlobalErrorMsg(sb.toString());
        } catch (Throwable ignored) {
            // setGlobalErrorMsg is best-effort; the log lines above are the real guarantee
        }
    }
}