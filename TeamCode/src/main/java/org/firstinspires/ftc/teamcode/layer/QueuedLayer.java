package org.firstinspires.teamcode.layer;

import java.util.Iterator;
import java.util.List;
import org.firstinspires.teamcode.layer.LayerSetupInfo;
import org.firstinspires.teamcode.task.Task;

/**
 * A convenience base class for layers that can compute queues of subtasks ahead of time and require
 * no additional processing in their update method.
 */
public abstract class QueuedLayer implements Layer {
    /**
     * An iterator over the subtasks for the current accepted task.
     */
    private Iterator<Task> subtaskIter;

    protected QueuedLayer() {
        subtaskIter = null;
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public boolean isTaskDone() {
        return subtaskIter == null || !subtaskIter.hasNext();
    }

    @Override
    public void update() {
        return subtaskIter.next();
    }

    /**
     * Sets the current list of subtasks to delegate.
     * @param subtasks - the list of subtasks.
     */
    protected void setSubtasks(List<Task> subtasks) {
        subtaskIter = subtasks.iterator();
    }
}
