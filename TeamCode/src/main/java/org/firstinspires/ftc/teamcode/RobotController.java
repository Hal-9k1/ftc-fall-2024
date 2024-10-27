package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Executes a Layer stack.
 * This forms the core of the robot's control logic: layers processing tasks by computing subtasks
 * and then delegating to subordinates.
 */
public class RobotController {
    private ArrayList<Consumer<Boolean>> updateListeners;
    private List<Layer> layers;

    /**
     * Constructs a RobotController.
     */
    public RobotController() {
        updateListeners = new ArrayList<>();
    }

    /**
     * Initializes the controller with the given layers.
     *
     * @param hardwareMap HardwareMap used to retrieve interfaces for robot hardware.
     * @param layers the layer stack to use.
     */
    public void setup(HardwareMap hardwareMap, List<Layer> layers) {
        LayerSetupInfo setupInfo = new LayerSetupInfo(hardwareMap);
        for (Layer layer : layers) {
            layer.setup(setupInfo);
        }
        this.layers = layers;
    }

    /**
     * Performs incremental work and returns whether layers have completed all tasks.
     * Performs incremental work on the bottommost layer of the configured stack, invoking upper
     * layers as necessary when lower layers complete their current tasks.
     *
     * @return whether the topmost layer (and by extension, the whole stack of layers) is exhausted
     * of tasks. When this happens, update listeners are notified and then unregistered.
     */
    public boolean update() {
        // Call all update listeners
        for (Consumer<Boolean> listener : updateListeners) {
            listener.consume(false);
        }

        // Do work on layers
        if (layers == null) {
            return true;
        }
        int i = 0;
        Task currentTask = null;
        for (Layer layer : layers) {
            if (!layer.isTaskDone()) {
                currentTask = layer.update();
                if (currentTask == null) {
                    throw new NullPointerException("Layer '" + layer.getClass().getName()
                        + "' returned null from update.");
                }
                break;
            }
            i++;
        }
        if (currentTask == null) {
            // No tasks left in any layer, inform all listeners of completion
            for (Consumer<Boolean> listener : updateListeners) {
                listener.consume(true);
            }
            updateListeners.clear();
            layers = null;
            return true;
        }
        for (int j = i - 1; j > -1; j--) {
            layers[j].acceptTask(currentTask);
            currentTask = layers[j].update();
            if (currentTask = null) {
                throw new NullPointerException("Layer '" + layers[j].getClass().getName()
                    + "' returned null from update.");
            }
            return false;
        }
    }

    /**
     * Registers a function to be called on every update.
     * Registers a function to be called on every update of the controller before layer work is
     * performed. Listeners are executed in registration order and called with a single positional
     * argument of true. On the first update after the topmost layer runs out of tasks, the
     * listeners are called again with an argument of false, then unregistered.
     *
     * @param listener the function to be registered as an update listener
     */
    public void addUpdateListener(Consumer<Boolean> listener) {
        updateListeners.add(listener);
    }
}
