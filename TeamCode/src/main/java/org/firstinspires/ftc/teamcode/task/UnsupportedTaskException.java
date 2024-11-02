package org.firstinspires.ftc.teamcode.task;

import org.firstinspires.ftc.teamcode.layer.Layer;

/**
 * Exception raised by layers when accepting a task type they don't support.
 */
public class UnsupportedTaskException extends IllegalArgumentException {
    /**
     * Constructs an UnsupportedTaskException.
     *
     * @param layer the Layer throwing the exception.
     * @param task the Task that the layer rejected.
     */
    public UnsupportedTaskException(Layer layer, Task task) {
        super("Layer '" + layer.getClass().getName() + "' does not support task of type '"
            + task.getClass().getName() + "'.");
    }
}
