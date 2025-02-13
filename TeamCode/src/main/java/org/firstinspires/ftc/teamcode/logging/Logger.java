package org.firstinspires.ftc.teamcode.logging;

import java.util.Date;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Reports log messages, updatable fields, and field plot items to a backend.
 */
public final class Logger {
    /**
     * The standard severity for errors.
     */
    static final String ERROR_SEVERITY = "ERROR";

    /**
     * The standard severity for messages generated in unusual but recoverable operation.
     */
    static final String WARN_SEVERITY = "WARN";

    /**
     * The standard severity for messages generated in ordinary operation.
     */
    static final String INFO_SEVERITY = "INFO";

    /**
     * The standard severity for excessively verbose messages used for debugging.
     */
    static final String TRACE_SEVERITY = "TRACE";

    /**
     * The unique label for this logger, attached as a tag to all logging events.
     */
    private final String label;

    /**
     * The backend that log events are sent to.
     */
    private final LoggerBackend backend;

    /**
     * The default severity to assign to log messages if none is explicitly specified.
     */
    private final String defaultSeverity;

    /**
     * Whether explicitly specifying severity for log messages is disabled.
     */
    private final boolean defaultSeverityOnly;

    /**
     * Contains which severities should report source code locations of logger method calls.
     */
    private final SeverityFilter reportLocationsFilter;

    /**
     * Contains which severities should attach the timestamp of the logger method call to the
     * message.
     */
    private final SeverityFilter reportTimestampFilter;

    /**
     * Constructs a Logger.
     * Users should create loggers by creating and configuring a {@link LoggerProvider} instance
     * and calling {@link LoggerProvider#getLogger}.
     *
     * @param label the unique label for this logger.
     * @param backend the backend to report log events to.
     * @param defaultSeverity the severity attached to log messages when none is explicitly
     * specified.
     * @param defaultSeverityOnly whether explicitly specifying severity for log messages is
     * disabled.
     * @param reportLocationsFilter contains which severities should report source code locations
     * of logger calls.
     * @param reportTimestampFilter contains which severities should attach the timestamp of the
     * logger method call to the message.
     */
    Logger(
        String label,
        LoggerBackend backend,
        String defaultSeverity,
        boolean defaultSeverityOnly,
        SeverityFilter reportLocationsFilter,
        SeverityFilter reportTimestampFilter
    ) {
        this.label = label;
        this.backend = backend;
        this.defaultSeverity = defaultSeverity;
        this.defaultSeverityOnly = defaultSeverityOnly;
        this.reportLocationsFilter = reportLocationsFilter;
        this.reportTimestampFilter = reportTimestampFilter;
    }

    /**
     * Explicitly logs a message at the standard error severity.
     *
     * @param args objects that will be converted to strings and concatenated to form the log message.
     */
    public void error(Object... args) {
        doLog(true, ERROR_SEVERITY, args);
    }

    /**
     * Explicitly logs a message at the standard warning severity.
     *
     * @param args objects that will be converted to strings and concatenated to form the log message.
     */
    public void warn(Object... args) {
        doLog(true, WARN_SEVERITY, args);
    }

    /**
     * Explicitly logs a message at the standard informational message severity.
     *
     * @param args objects that will be converted to strings and concatenated to form the log message.
     */
    public void info(Object... args) {
        doLog(true, INFO_SEVERITY, args);
    }

    /**
     * Explicitly logs a message at the standard verbose debugging message severity.
     *
     * @param args objects that will be converted to strings and concatenated to form the log message.
     */
    public void trace(Object... args) {
        doLog(true, TRACE_SEVERITY, args);
    }

    /**
     * Explicitly logs a message at the specified severity.
     *
     * @param severity the severity to log the message at.
     * @param args objects that will be converted to strings and concatenated to form the log message.
     */
    public void logSeverity(String severity, Object... args) {
        doLog(true, severity, args);
    }

    /**
     * Logs a message at this logger's default severity.
     *
     * @param args objects that will be converted to strings and concatenated to form the log message.
     */
    public void log(Object... args) {
        doLog(false, defaultSeverity, args);
    }

    /**
     * Logs an absolute position plot.
     *
     * @param itemLabel the unique label of the plot.
     * @param position the position to plot.
     */
    public void position(String itemLabel, Vec2 position) {
        backend.processPosition(label, itemLabel, position);
    }

    /**
     * Logs a vector plot.
     *
     * @param itemLabel the unique label of the plot.
     * @param attachLabel the label of a plot to plot the vector relative to,
     * or null if the vector should be plotted as-is.
     * @param vector the vector to plot.
     */
    public void vector(String itemLabel, String attachLabel, Vec2 vector) {
        backend.processVector(label, itemLabel, attachLabel, vector);
    }

    /**
     * Logs a transformation plot.
     *
     * @param itemLabel the unique label of the plot.
     * @param attachLabel the label of a plot to plot the transformation relative to, or null if
     * the transformation should be plotted as-is.
     * @param transform the transformation to plot.
     */
    public void transform(String itemLabel, String attachLabel, Mat3 transform) {
        backend.processTransform(label, itemLabel, attachLabel, transform);
    }

    /**
     * Logs an update to an updatable field.
     *
     * @param itemLabel the unique label of the field.
     * @param object the object whose string representation is the new value of the field.
     */
    public void update(String itemLabel, Object object) {
        backend.processUpdatableObject(label, itemLabel, object);
    }

    /**
     * Creates a log message from the arguments at the given severity and sends it to the backend.
     *
     * @param explicitSeverity whether an explicit severity was provided.
     * @param severity the severity to log the message at, which may be the default.
     * @param args the arguments to convert to strings and concatenate to form the log message.
     */
    private void doLog(boolean explicitSeverity, String severity, Object[] args) {
        if (explicitSeverity && defaultSeverityOnly) {
            throw new IllegalArgumentException("Attempt to log with explicit severity on logger"
                + " configured to allow default severity only.");
        }
        String location = null;
        if (reportLocationsFilter.permit(severity)) {
            boolean foundSelf = false;
            for (StackTraceElement frame : Thread.currentThread().getStackTrace()) {
                if (frame.getClassName() == getClass().getName()) {
                    // This may trigger multiple times
                    foundSelf = true;
                } else if (foundSelf) {
                    location = frame.getFileName() + ":" + frame.getLineNumber();
                }
            }
            // Location might still be null by now
        }
        String timestamp = "";
        if (reportTimestampFilter.permit(severity)) {
            timestamp = String.format("%tT.%<tL ", new Date());
        }
        StringBuilder msg = new StringBuilder("[");
        msg.append(timestamp);
        msg.append(severity);
        msg.append(" ");
        msg.append(label);
        if (location != null) {
            msg.append(" ");
            msg.append(location);
        }
        msg.append("] ");
        for (Object arg : args) {
            msg.append(arg);
            msg.append(" ");
        }
        backend.processLog(new Log(severity, label, location, msg.toString()));
    }
}
