package org.firstinspires.ftc.teamcode.logging;

public class LoggerProvider {
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
        useDefaultSeverityOnly = false;
        useTimestamp = true;
        timestampExceptions = new HashSet<>();
        useLocation = false;
        locationExceptions = new HashSet<>();
    }

    public LoggerProvider clone() {
        LoggerProvider copy = new LoggerProvider();
        copy.backends = backends;
        copy.defaultSeverityName = defaultSeverityName;
        copy.useDefaultSeverityOnly = useDefaultSeverityOnly;
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
                void processPosition(String loggerLabel, String itemLabel, Vec2 position) { }

                @Override
                void processVector(String loggerLabel, String itemLabel, String attachLabel,
                    Vec2 vector) { }

                @Override
                void processTransform(String loggerLabel, String itemLabel, String attachLabel,
                    Mat3 transform) { }

                @Override
                void processUpdatableObject(String loggerLabel, String itemLabel, Object object) { }

                @Override
                void processLog(Log log) { }
            };
        } else if (backends.size() == 1) {
            backend = backends.get(0);
        } else {
            // Aggregate backend
            ArrayList<LoggerBackend> backendListCopy = new ArrayList<>(backends);
            backend = new LoggerBackend() {
                @Override
                void processPosition(String loggerLabel, String itemLabel, Vec2 position) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processPosition(loggerLabel, itemLabel, position);
                    }
                }

                @Override
                void processVector(String loggerLabel, String itemLabel, String attachLabel,
                    Vec2 vector) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processVector(loggerLabel, itemLabel, attachLabel, position);
                    }
                }

                @Override
                void processTransform(String loggerLabel, String itemLabel, String attachLabel,
                    Mat3 transform) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processPosition(loggerLabel, itemLabel, attachLabel,
                            transform);
                    }
                }

                @Override
                void processUpdatableObject(String loggerLabel, String itemLabel, Object object) {
                    for (LoggerBackend innerBackend : backendListCopy) {
                        innerBackend.processUpdatableObject(loggerLabel, itemLabel, object);
                    }
                }

                @Override
                void processLog(Log log) {
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
                timetstampExceptions
            )
        );
    }

    public LoggerProvider addBackend(LoggerBackend backend) {
        backends.add(backend);
    }

    public LoggerProvider defaultSeverity(String severity) {
        defaultSeverityName = severity;
    }

    public LoggerProvider defaultSeverityError() {
        defaultSeverityName = Logger.ERROR_SEVERITY;
    }

    public LoggerProvider defaultSeverityWarn() {
        defaultSeverityName = Logger.WARN_SEVERITY;
    }

    public LoggerProvider defaultSeverityInfo() {
        defaultSeverityName = Logger.INFO_SEVERITY;
    }

    public LoggerProvider defaultSeverityTrace() {
        defaultSeverityName = Logger.TRACE_SEVERITY;
    }

    public LoggerProvider useDefaultSeverityOnly(boolean enable) {
        defaultSeverityOnly = enable;
    }

    public LoggerProvider timestamp(boolean enable) {
        useTimestamp = enable;
        timestampExceptions.clear();
    }

    public LoggerProvider exceptTimestamp(String severity) {
        timestampExceptions.add(severity);
    }

    public LoggerProvider exceptTimestampError() {
        timestampExceptions.add(Logger.ERROR_SEVERITY);
    }

    public LoggerProvider exceptTimestampWarn() {
        timestampExceptions.add(Logger.WARN_SEVERITY);
    }

    public LoggerProvider exceptTimestampInfo() {
        timestampExceptions.add(Logger.INFO_SEVERITY);
    }

    public LoggerProvider exceptTimestampTrace() {
        timestampExceptions.add(Logger.TRACE_SEVERITY);
    }

    public LoggerProvider location(boolean enable) {
        useLocation = enable;
        locationExceptions.clear();
    }

    public LoggerProvider exceptLocation(String severity) {
        locationExceptions.add(severity);
    }

    public LoggerProvider exceptLocationError() {
        locationExceptions.add(Logger.ERROR_SEVERITY);
    }

    public LoggerProvider exceptLocationWarn() {
        locationExceptions.add(Logger.WARN_SEVERITY);
    }

    public LoggerProvider exceptLocationInfo() {
        locationExceptions.add(Logger.INFO_SEVERITY);
    }

    public LoggerProvider exceptLocationTrace() {
        locationExceptions.add(Logger.TRACE_SEVERITY);
    }
}
