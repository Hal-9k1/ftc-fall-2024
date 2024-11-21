package org.firstinspires.ftc.teamcode.layer;

import java.util.Collections;
import java.util.Iterator;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Convenience base class for all layers that map each accepted task to only one subtask.
 * Similar to QueuedLayer, but only generates one task instead of a queue.
 */
public abstract class FunctionLayer implements Layer {
    private boolean emittedSubtask;
    private Task subtask;

    protected FunctionLayer() {
        emittedSubtask = true;
        subtask = null;
    }

    @Override
    public boolean isTaskDone() {
        return emittedSubtask;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        emittedSubtask = true;
        return Collections.singleton(subtask).iterator();
    }

    @Override
    public void acceptTask(Task task) {
        subtask = map(task);
        emittedSubtask = false;
    }

    /**
     * Maps an accepted task to a subtask.
     * @param task The accepted task.
     * @return The supertask generated from the accepted task.
     */
    protected abstract Task map(Task task);
}
