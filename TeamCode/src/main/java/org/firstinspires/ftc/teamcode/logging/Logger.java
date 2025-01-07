package org.firstinspires.ftc.teamcode.logging;

import java.util.Date;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

public final class Logger {
    static final String ERROR_SEVERITY = "ERROR";

    static final String WARN_SEVERITY = "WARN";

    static final String INFO_SEVERITY = "INFO";

    static final String TRACE_SEVERITY = "TRACE";

    private final String label;

    private final LoggerBackend backend;

    private final String defaultSeverity;

    private final boolean defaultSeverityOnly;

    private final SeverityFilter reportLocationsFilter;

    private final SeverityFilter reportTimestampFilter;

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

    public void error(Object... args) {
        doLog(true, ERROR_SEVERITY, args);
    }

    public void warn(Object... args) {
        doLog(true, WARN_SEVERITY, args);
    }

    public void info(Object... args) {
        doLog(true, INFO_SEVERITY, args);
    }

    public void trace(Object... args) {
        doLog(true, TRACE_SEVERITY, args);
    }

    public void logSeverity(String severity, Object... args) {
        doLog(true, severity, args);
    }

    public void log(Object... args) {
        doLog(false, defaultSeverity, args);
    }

    public void position(String itemLabel, Vec2 position) {
        backend.processPosition(label, itemLabel, position);
    }

    public void vector(String itemLabel, String attachLabel, Vec2 vector) {
        backend.processVector(label, itemLabel, attachLabel, vector);
    }

    public void transform(String itemLabel, String attachLabel, Mat3 transform) {
        backend.processTransform(label, itemLabel, attachLabel, transform);
    }

    public void update(String itemLabel, Object object) {
        backend.processUpdatableObject(label, itemLabel, object);
    }

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
