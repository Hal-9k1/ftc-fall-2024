package org.firstinspires.ftc.teamcode.logging;

import java.io.IOException;
import java.io.OutputStream;

import org.firstinspires.ftc.teamcode.IOUtil;

/**
 * A formatted log message.
 */
public final class Log {
    /**
     * The severity of the message.
     */
    private String severity;

    /**
     * The label attached to the message.
     * This is specified when obtaining a Logger instance from {@link LoggerProvider#getLogger}.
     */
    private String label;

    /**
     * If not null, the source location producing this message.
     */
    private String location;

    /**
     * The message text.
     */
    private String msg;

    /**
     * Constructs a Log.
     * Parameters must contain only ASCII characters.
     *
     * @param severity - the severity of the message.
     * @param label - the label attached to the message, unique to the Logger that produced it.
     * @param location - if not null, a string of the format "filename:line_no" describing the
     * source location that produced this message.
     * @param msg - the message text.
     */
    Log(String severity, String label, String location, String msg) {
        this.severity = severity;
        this.label = label;
        this.location = location;
        this.msg = msg;
    }

    /**
     * Returns the message text of the log message.
     *
     * @return the message text.
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Serializes this message and writes it to a stream.
     *
     * @throws IOException - an I/O error occurred while writing to the stream.
     */
    public void writeTo(OutputStream stream) throws IOException {
        IOUtil.writeFlexibleString(stream, severity);
        IOUtil.writeFlexibleString(stream, label);
        IOUtil.writeFlexibleString(stream, location);
        IOUtil.writeFlexibleString(stream, msg);
    }
}
