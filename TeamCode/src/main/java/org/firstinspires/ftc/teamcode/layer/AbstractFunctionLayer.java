package org.firstinspires.ftc.teamcode.layer;

import java.util.Collections;
import java.util.Iterator;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Convenience base class for all layers that map each accepted task to only one subtask.
 * Similar to {@link AbstractQueuedLayer}, but only generates one task instead of a queue.
 */
public abstract class AbstractFunctionLayer implements Layer {
    /**
     * Whether {@link #subtask} has been emitted yet.
     */
    private boolean emittedSubtask;

    /**
     * The next subtask to emit, generated by calling {@link #map} on the last accepted task.
     */
    private Task subtask;

    /**
     * Constructs a AbstractFunctionLayer.
     */
    protected AbstractFunctionLayer() {
        emittedSubtask = true;
        subtask = null;
    }

    @Override
    public final boolean isTaskDone() {
        return emittedSubtask;
    }

    @Override
    public final Iterator<Task> update(Iterable<Task> completed) {
        if (emittedSubtask) {
            throw new IllegalStateException(
                String.format(
                    "FunctionLayer '%s' updated without new subtask.",
                    getClass().getSimpleName()
                )
            );
        }
        emittedSubtask = true;
        return Collections.singleton(subtask).iterator();
    }

    @Override
    public final void acceptTask(Task task) {
        subtask = map(task);
        if (subtask == null) {
            throw new NullPointerException(
                String.format(
                    "FunctionLayer '%s' returned null from mapping function.",
                    getClass().getSimpleName()
                )
            );
        }
        emittedSubtask = false;
    }

    /**
     * Maps an accepted task to a subtask.
     *
     * @param task The accepted task.
     * @return The subtask generated from the accepted task.
     */
    protected abstract Task map(Task task);
}
