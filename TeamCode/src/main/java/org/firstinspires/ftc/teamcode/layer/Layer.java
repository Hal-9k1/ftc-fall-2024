package org.firstinspires.ftc.teamcode.layer;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

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
     * Performs layer setup that requires access to hardware and the RobotController.
     * @param setup - LayerSetupInfo provided to set up the layer.
     */
    void setup(LayerSetupInfo setup);

    /**
     * Returns whether the layer is ready to accept a new task.
     * This method shouldbe free of any side effects; move code mutating state to update or
     * acceptTask.
     * @return true if the layer has finished processing the last accepted task, if any.
     */
    boolean isTaskDone();

    /**
     * Returns the next subordinate task produced from this layer's current task.
     * Calculates the next subordinate task that should be submitted to the below layer. The return
     * value of a bottom layer's update function is not used.
     * @return The next task that the lower layer should run. Must not be null unless this is the
     * bottommost layer.
     */
    Task update();

    /**
     * Sets the layer's current task.
     * Accepts a task from the above layer.
     * Behavior is only defined if {@link Layer#isTaskDone} returns true.
     * @param task - the task this layer should start processing.
     * @throws UnsupportedTaskException - this layer does not support the given task.
     * Implementations must not change the internal state of the layer if this is thrown; isTaskDone
     * should still return true.
     */
    void acceptTask(Task task);
}
