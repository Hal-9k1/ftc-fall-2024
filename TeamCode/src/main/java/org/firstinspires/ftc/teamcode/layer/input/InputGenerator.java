package org.firstinspires.ftc.teamcode.layer.input;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * The base class of input generating teleop layers.
 * Input generators will be asked for a new task every robot update. This task should be to process
 * the just-captured user input stored. Input generating layers must never present as done, which
 * would prevent the layer and everything below it from ever again being updated by the controller
 * in the likely case that the input generator is the top layer.
 */
public abstract class InputGenerator implements Layer {
    @Override
    public boolean isTaskDone() {
        return false;
    }

    @Override
    public void acceptTask(Task task) {
        throw new UnsupportedTaskException(this, task);
    }
}

