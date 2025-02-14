package org.firstinspires.ftc.teamcode.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * A builder for {@link Logger} instances that supports hierarchal configuration.
 */
public final class LoggerProvider {
    /**
     * The list of backends built loggers should report to.
     */
    private List<LoggerBackend> backends;

    /**
     * The default severity of built loggers.
     */
    private String defaultSeverityName;

    /**
     * Whether built loggers may only log using the default severity.
     */
    private boolean defaultSeverityOnly;

    /**
     * Whether built loggers should attach timestamps to log messages.
     */
    private boolean useTimestamp;

    /**
     * Severities that are exceptions to whatever is specified by {@link #useTimestamp}.
     */
    private Set<String> timestampExceptions;

    /**
     * Whether built loggers should attach source code locations to log messages.
     */
    private boolean useLocation;

    /**
     * Severities that are exceptions to whatever is specified by {@link #useLocation}.
     */
    private Set<String> locationExceptions;

    /**
     * Constructs a LoggerProvider.
     */
    public LoggerProvider() {
        backends = new ArrayList<>();
        defaultSeverityName = Logger.INFO_SEVERITY;
        defaultSeverityOnly = false;
        useTimestamp = true;
        timestampExceptions = new HashSet<>();
        useLocation = false;
        locationExceptions = new HashSet<>();
    }

    public LoggerProvider clone() {
        LoggerProvider copy = new LoggerProvider();
        copy.backends = backends;
        copy.defaultSeverityName = defaultSeverityName;
        copy.defaultSeverityOnly = defaultSeverityOnly;
        copy.useTimestamp = useTimestamp;
        copy.timestampExceptions = timestampExceptions;
        copy.useLocation = useLocation;
        copy.locationExceptions = locationExceptions;
        return copy;
    }

    /**
     * Constructs a logger with the current configuration of this LoggerProvider.
     *
     * @param label the unique label to give the logger.
     * @return The constructed logger.
     */
    public Logger getLogger(String label) {
        LoggerBackend backend;
        if (backends.size() == 0) {
            // No-op backend
            backend = new LoggerBackend() {
                @Override
                public void close() { }

                @Override
                public void processPosition(String loggerLabel, String itemLabel, Vec2 position) { }

                @Override
                public void processVector(String loggerLabel, String itemLabel, String attachLabel,
                    Vec2 vector) { }

                @Override
                public void processTransform(String loggerLabel, String itemLabel, String attachLabel,
                    Mat3 transform) { }

                @Override
                public void processUpdatableObject(String loggerLabel, String itemLabel, Object object) { }

                @Override
                public void processLog(Log log) { }
            };
        } else if (backends.size() == 1) {
            backend = backends.get(0);
        } else {
            // Aggregate backend
            ArrayList<LoggerBackend> backendListCopy = new ArrayList<>(backends);
            backend = new LoggerBackend() {
                @Override
                public void close() throws IOException {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.close();
                    }
                }

                @Override
                public void processPosition(String loggerLabel, String itemLabel, Vec2 position) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processPosition(loggerLabel, itemLabel, position);
                    }
                }

                @Override
                public void processVector(String loggerLabel, String itemLabel, String attachLabel,
                    Vec2 vector) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processVector(loggerLabel, itemLabel, attachLabel, vector);
                    }
                }

                @Override
                public void processTransform(String loggerLabel, String itemLabel,
                    String attachLabel, Mat3 transform) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processTransform(loggerLabel, itemLabel, attachLabel,
                            transform);
                    }
                }

                @Override
                public void processUpdatableObject(String loggerLabel, String itemLabel,
                    Object object) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processUpdatableObject(loggerLabel, itemLabel, object);
                    }
                }

                @Override
                public void processLog(Log log) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processLog(log);
                    }
                }
            };
        }
        return new Logger(
            label,
            backend,
            defaultSeverityName,
            defaultSeverityOnly,
            new SeverityFilter(
                useLocation,
                locationExceptions
            ),
            new SeverityFilter(
                useTimestamp,
                timestampExceptions
            )
        );
    }

    /**
     * Adds a backend to the list of backends built loggers will report to.
     *
     * @param backend the backend to add.
     * @return This LoggerProvider.
     */
    public LoggerProvider addBackend(LoggerBackend backend) {
        backends.add(backend);
        return this;
    }

    /**
     * Sets the default severity of built loggers.
     *
     * @param severity the new default severity.
     * @return This LoggerProvider.
     */
    public LoggerProvider defaultSeverity(String severity) {
        defaultSeverityName = severity;
        return this;
    }

    /**
     * Sets the default severity of built loggers to the standard error-level severity.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider defaultSeverityError() {
        defaultSeverityName = Logger.ERROR_SEVERITY;
        return this;
    }

    /**
     * Sets the default severity of built loggers to the standard warning-level severity.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider defaultSeverityWarn() {
        defaultSeverityName = Logger.WARN_SEVERITY;
        return this;
    }

    /**
     * Sets the default severity of built loggers to the standard information message severity.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider defaultSeverityInfo() {
        defaultSeverityName = Logger.INFO_SEVERITY;
        return this;
    }

    /**
     * Sets the default severity of built loggers to the standard verbose debugging message severity.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider defaultSeverityTrace() {
        defaultSeverityName = Logger.TRACE_SEVERITY;
        return this;
    }

    /**
     * Whether built loggers may only log using the default severity.
     *
     * @param enable whether calling {@link Logger#error}, {@link Logger#warn}, {@link Logger#info},
     * {@link Logger#trace}, or {@link Logger#logSeverity} should result in an error.
     * @return This LoggerProvider.
     */
    public LoggerProvider useDefaultSeverityOnly(boolean enable) {
        defaultSeverityOnly = enable;
        return this;
    }

    /**
     * Sets whether built loggers should attach timestamps to log messages.
     *
     * @param enable whether timestamps should be attached.
     * @return This LoggerProvider.
     */
    public LoggerProvider timestamp(boolean enable) {
        useTimestamp = enable;
        timestampExceptions.clear();
        return this;
    }

    /**
     * Makes the given severity an exception to whatever rule for attaching timestamps was specified
     * by {@link #timestamp}.
     *
     * @param severity the severity to add as an exception.
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptTimestamp(String severity) {
        timestampExceptions.add(severity);
        return this;
    }

    /**
     * Makes the standard error severity an exception to whatever rule for attaching timestamps was
     * specified by {@link #timestamp}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptTimestampError() {
        timestampExceptions.add(Logger.ERROR_SEVERITY);
        return this;
    }

    /**
     * Makes the standard warning severity an exception to whatever rule for attaching timestamps
     * was specified by {@link #timestamp}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptTimestampWarn() {
        timestampExceptions.add(Logger.WARN_SEVERITY);
        return this;
    }

    /**
     * Makes the standard informational message severity an exception to whatever rule for attaching
     * timestamps was specified by {@link #timestamp}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptTimestampInfo() {
        timestampExceptions.add(Logger.INFO_SEVERITY);
        return this;
    }

    /**
     * Makes the standard verbose debugging message severity severity an exception to whatever rule
     * for attaching timestamps was specified by {@link #timestamp}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptTimestampTrace() {
        timestampExceptions.add(Logger.TRACE_SEVERITY);
        return this;
    }

    /**
     * Sets whether built loggers should attach source code locations to log messages.
     *
     * @param enable whether source code locations should be attached.
     * @return This LoggerProvider.
     */
    public LoggerProvider location(boolean enable) {
        useLocation = enable;
        locationExceptions.clear();
        return this;
    }

    /**
     * Makes the given severity an exception to whatever rule for attaching source code locations
     * was specified by {@link #location}.
     *
     * @param severity the severity to add as an exception.
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptLocation(String severity) {
        locationExceptions.add(severity);
        return this;
    }

    /**
     * Makes the standard error severity an exception to whatever rule for attaching source code
     * locations was specified by {@link #location}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptLocationError() {
        locationExceptions.add(Logger.ERROR_SEVERITY);
        return this;
    }

    /**
     * Makes the standard warning severity an exception to whatever rule for attaching source code
     * locations was specified by {@link #location}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptLocationWarn() {
        locationExceptions.add(Logger.WARN_SEVERITY);
        return this;
    }

    /**
     * Makes the standard informational message severity an exception to whatever rule for attaching
     * source code locations was specified by {@link #location}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptLocationInfo() {
        locationExceptions.add(Logger.INFO_SEVERITY);
        return this;
    }

    /**
     * Makes the standard verbose debugging message severity an exception to whatever rule for
     * attaching source code locations was specified by {@link #location}.
     *
     * @return This LoggerProvider.
     */
    public LoggerProvider exceptLocationTrace() {
        locationExceptions.add(Logger.TRACE_SEVERITY);
        return this;
    }
}
