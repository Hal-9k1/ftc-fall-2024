package org.firstinspires.ftc.teamcode.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

public final class LoggerProvider {
    private List<LoggerBackend> backends;

    private String defaultSeverityName;

    private boolean defaultSeverityOnly;

    private boolean useTimestamp;

    private Set<String> timestampExceptions;

    private boolean useLocation;

    private Set<String> locationExceptions;

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

    public LoggerProvider addBackend(LoggerBackend backend) {
        backends.add(backend);
        return this;
    }

    public LoggerProvider defaultSeverity(String severity) {
        defaultSeverityName = severity;
        return this;
    }

    public LoggerProvider defaultSeverityError() {
        defaultSeverityName = Logger.ERROR_SEVERITY;
        return this;
    }

    public LoggerProvider defaultSeverityWarn() {
        defaultSeverityName = Logger.WARN_SEVERITY;
        return this;
    }

    public LoggerProvider defaultSeverityInfo() {
        defaultSeverityName = Logger.INFO_SEVERITY;
        return this;
    }

    public LoggerProvider defaultSeverityTrace() {
        defaultSeverityName = Logger.TRACE_SEVERITY;
        return this;
    }

    public LoggerProvider useDefaultSeverityOnly(boolean enable) {
        defaultSeverityOnly = enable;
        return this;
    }

    public LoggerProvider timestamp(boolean enable) {
        useTimestamp = enable;
        timestampExceptions.clear();
        return this;
    }

    public LoggerProvider exceptTimestamp(String severity) {
        timestampExceptions.add(severity);
        return this;
    }

    public LoggerProvider exceptTimestampError() {
        timestampExceptions.add(Logger.ERROR_SEVERITY);
        return this;
    }

    public LoggerProvider exceptTimestampWarn() {
        timestampExceptions.add(Logger.WARN_SEVERITY);
        return this;
    }

    public LoggerProvider exceptTimestampInfo() {
        timestampExceptions.add(Logger.INFO_SEVERITY);
        return this;
    }

    public LoggerProvider exceptTimestampTrace() {
        timestampExceptions.add(Logger.TRACE_SEVERITY);
        return this;
    }

    public LoggerProvider location(boolean enable) {
        useLocation = enable;
        locationExceptions.clear();
        return this;
    }

    public LoggerProvider exceptLocation(String severity) {
        locationExceptions.add(severity);
        return this;
    }

    public LoggerProvider exceptLocationError() {
        locationExceptions.add(Logger.ERROR_SEVERITY);
        return this;
    }

    public LoggerProvider exceptLocationWarn() {
        locationExceptions.add(Logger.WARN_SEVERITY);
        return this;
    }

    public LoggerProvider exceptLocationInfo() {
        locationExceptions.add(Logger.INFO_SEVERITY);
        return this;
    }

    public LoggerProvider exceptLocationTrace() {
        locationExceptions.add(Logger.TRACE_SEVERITY);
        return this;
    }
}
