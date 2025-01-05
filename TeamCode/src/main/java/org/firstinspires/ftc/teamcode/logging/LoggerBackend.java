package org.firstinspires.ftc.teamcode.logging;

import java.io.Closeable;
import java.util.List;

import org.firstinspires.ftc.teamcode.matrix.Mat3;
import org.firstinspires.ftc.teamcode.matrix.Vec2;

/**
 * Processes logging events collected from the program by a {@link Logger}.
 */
public interface LoggerBackend extends Closeable {
    /**
     * Processes an optionally labeled position.
     * @param loggerLabel - the label of the logger reporting this item.
     * @param itemLabel - the label to assign to the position, or null if it should be unlabeled.
     * @param position - the position to process.
     */
    void processPosition(String loggerLabel, String itemLabel, Vec2 position);

    /**
     * Processes an optionally labeled vector.
     * @param loggerLabel - the label of the logger reporting this item.
     * @param itemLabel - the label to assign to the vector, or null if it should be unlabeled.
     * @param attachLabel - the label of the position, transform, or other vector to anchor this
     * transform to.
     * @param vector - the vector to process.
     */
    void processVector(String loggerLabel, String itemLabel, String attachLabel, Vec2 vector);

    /**
     * Processes an optionally labeled and anchored transform.
     * @param loggerLabel - the label of the logger reporting this item.
     * @param itemLabel - the label to assign to the transform, or null if it should be unlabeled.
     * @param attachLabel - the label of the position, vector, or other transform to anchor this
     * transform to.
     * @param transform - the transform to process. The bottom row is discarded, as in a 3x3
     * transformation matrix it holds no meaningful information.
     */
    void processTransform(String loggerLabel, String itemLabel, String attachLabel, Mat3 transform);

    /**
     * Processes the string representation of a labeled value tracked over time.
     * @param loggerLabel - the label of the logger reporting this item.
     * @param itemLabel - the label to assign to the value.
     * @param object - the object whose string representation is being tracked.
     */
    void processUpdatableObject(String loggerLabel, String itemLabel, Object object);

    /**
     * Processes a formatted log message.
     * @param logs - the log message to process.
     */
    void processLog(Log log);
}
