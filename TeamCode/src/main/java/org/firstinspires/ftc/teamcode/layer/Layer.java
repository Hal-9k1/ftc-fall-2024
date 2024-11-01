package org.firstinspires.ftc.teamcode.layer;

import org.firstinspires.ftc.teamcode.task.Task;

/**
 * A modular unit of robot functionality.
 * Layers interact with other layers by accepting tasks from the above layer and submitting tasks to
 * the below layer. This represents the breaking down of a complex or abstract task into more simple
 * and concrete ones. (A task is an instruction passed from a layer to its subordinate. Tasks range
 * widely in their concreteness and can be as vague as "win the game" or as specific as "move
 * forward 2 meters.")
 */
public interface Layer {
    /**
     * Returns whether the layer is ready to accept a new task.
     * @return true if the layer has finished processing the last accepted task, if any.
     */
    boolean isTaskDone();
    /**
     * Returns the next subordinate task produced from this layer's current task.
     * Calculates the next subordinate task that should be submitted to the below layer. The return
     * value of a bottom layer's update function is not used.
     * @return The next task that the lower layer should run.
     */
    Task update();
    /**
     * Sets the layer's current task.
     * Accepts a task from the above layer. Should only be called after {@link Layer#isTaskDone}
     * returns True.
     * @param task the task this layer should start processing.
     */
    void acceptTask(Task task);
}
