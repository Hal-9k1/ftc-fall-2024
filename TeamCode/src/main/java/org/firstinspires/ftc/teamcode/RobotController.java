package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Executes a Layer stack.
 * This forms the core of the robot's control logic: layers processing tasks by computing subtasks
 * and then delegating to subordinates.
 * To process a "tick" on the layer stack, RobotController:
 * - Finds the bottommost layer whose {@link Layer.isTaskDone} method returns false, indicating that
 *   it has more subtasks to emit.
 * - Requests a new subtask from it with the {@link Layer.update} method, which is then given to the
 *   layer below it in the stack with {@link Layer.acceptTask} method. A layer may supply more than
 *   one subtask in this step, in which case the layer below it is offered each of the emitted
 *   subtasks while its isTaskDone method still returns true. If the lower layer's isTaskDone method
 *   returns false while there are still tasks to be consumed, an exception is thrown.
 * - Applies the preceeding step to each lower layer in turn, "trickling down" the new subtasks. The
 *   return value of the bottommost layer's update method is ignored; this is assumed to be a drive
 *   layer that does not produce any tasks to delegate.
 * Through creative Layer implementations such as
 * {@link org.firstinspires.ftc.teamcode.layer.MultiplexLayer}, this system enables complex logic to
 * be described modularly and with loose coupling.
 */
public class RobotController {
    private ArrayList<Consumer<Boolean>> updateListeners;
    private List<Layer> layers;

    /**
     * Constructs a RobotController.
     */
    public RobotController() {
        updateListeners = new ArrayList<>();
        layers = null;
    }

    /**
     * Initializes the controller with the given layers.
     * @param hardwareMap HardwareMap used to retrieve interfaces for robot hardware.
     * @param layers the layer stack to use.
     * @param gamepad0 the first connected Gamepad, or null if none is connected or available.
     * @param gamepad1 the second connected Gamepad, or null if none is connected or available.
     */
    public void setup(HardwareMap hardwareMap, List<Layer> layers, Gamepad gamepad0,
        Gamepad gamepad1) {
        LayerSetupInfo setupInfo = new LayerSetupInfo(hardwareMap, this, gamepad0, gamepad1);
        for (Layer layer : layers) {
            layer.setup(setupInfo);
        }
        this.layers = layers;
    }

    /**
     * Performs incremental work and returns whether layers have completed all tasks.
     * Performs incremental work on the bottommost layer of the configured stack, invoking upper
     * layers as necessary when lower layers complete their current tasks.
     * @return whether the topmost layer (and by extension, the whole stack of layers) is exhausted
     * of tasks. When this happens, update listeners are notified and then unregistered.
     */
    public boolean update() {
        // Call all update listeners
        for (Consumer<Boolean> listener : updateListeners) {
            listener.accept(false);
        }

        // Do work on layers
        if (layers == null) {
            return true;
        }
        int i = 0;
        Layer layer = null;
        ListIterator<Layer> layerIter = layers.listIterator();
        while (true) {
            layer = layerIter.next();
            if (!layer.isTaskDone()) {
                break;
            }
            if (!layerIter.hasNext()) {
                // No tasks left in any layer, inform all listeners of completion
                for (Consumer<Boolean> listener : updateListeners) {
                    listener.accept(true);
                }
                updateListeners.clear();
                layers = null;
                return true;
            }
        }
        Iterator<Task> tasks;
        while (true) {
            tasks = layer.update();
            if (tasks == null) {
                throw new NullPointerException("Layer '" + layer.getClass().getName()
                    + "' returned null from update.");
            }
            if (!layerIter.hasPrevious()) {
                break;
            }
            layer = layerIter.previous();
            while (tasks.hasNext() && layer.isTaskDone()) {
                layer.acceptTask(tasks.next());
            }
            if (tasks.hasNext()) {
                String errMsg = "Layer '" + layer.getClass().getName() + "' did not consume all"
                    + " tasks from upper layer. Remaining tasks: ";
                for (int i = 0; i < MAX_UNCONSUMED_REPORT_TASKS && tasks.hasNext(); ++i) {
                    errMsg += tasks.next().getClass().getName() + (tasks.hasNext() ? ", " : "");
                }
                if (tasks.hasNext()) {
                    errMsg += " (and more)";
                }
                throw new UnsupportedTaskException(errMsg);
            }
        return false;
    }

    /**
     * Registers a function to be called on every update.
     * Registers a function to be called on every update of the controller before layer work is
     * performed. Listeners are executed in registration order and called with a single positional
     * argument of true. On the first update after the topmost layer runs out of tasks, the
     * listeners are called again with an argument of false, then unregistered.
     * @param listener the function to be registered as an update listener
     */
    public void addUpdateListener(Consumer<Boolean> listener) {
        updateListeners.add(listener);
    }
}
