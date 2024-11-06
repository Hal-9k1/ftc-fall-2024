package org.firstinspires.ftc.teamcode.layer;

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
    public Task update() {
        emittedSubtask = true;
        return subtask;
    }

    @Override
    public void acceptTask(Task task) {
        emittedSubtask = false;
        subtask = map(task);
    }

    /**
     * Maps an accepted task to a subtask.
     * @param task The accepted task.
     * @return The supertask generated from the accepted task.
     */
    protected abstract Task map(Task task);
}
