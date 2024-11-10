package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.layer.input.GamepadInputGenerator;
import org.firstinspires.ftc.teamcode.task.Task;

/**
 * Executes a Layer stack.
 * This forms the core of the robot's control logic: layers processing tasks by computing subtasks
 * and then delegating to subordinates.
 */
public class RobotController {
    private final ArrayList<Consumer<Boolean>> updateListeners;
    private List<Layer> layers;
    private Telemetry telemetry;
    private boolean debug;

    /**
     * Constructs a RobotController.
     */
    public RobotController() {
        updateListeners = new ArrayList<>();
        layers = null;
        telemetry = null;
    }

    /**
     * Initializes the controller with the given layers.
     * @param hardwareMap HardwareMap used to retrieve interfaces for robot hardware.
     * @param telemetry Telemetry used to send logs back to the Driver Hub.
     * @param layers the layer stack to use.
     * @param gamepad0 the first connected Gamepad, or null if none is connected or available.
     * @param gamepad1 the second connected Gamepad, or null if none is connected or available.
     */
    public void setup(HardwareMap hardwareMap, Telemetry telemetry, List<Layer> layers,
        Gamepad gamepad0, Gamepad gamepad1) {
        LayerSetupInfo setupInfo = new LayerSetupInfo(hardwareMap, telemetry, this, gamepad0,
            gamepad1);
        for (Layer layer : layers) {
            layer.setup(setupInfo);
        }
        this.layers = layers;
        this.telemetry = telemetry;
        debug = !layers.stream().anyMatch(x -> x instanceof GamepadInputGenerator);
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
            listener.accept(false);
        }

        // Do work on layers
        if (layers == null) {
            return true;
        }
        int i = 0;
        Layer layer;
        ListIterator<Layer> layerIter = layers.listIterator();
        while (true) {
            layer = layerIter.next();
            if (!layer.isTaskDone()) {
                break;
            }
            if (debug) {
                telemetry.log().add("Layer %s requests next task", layer.getClass().getName());
            }
            if (!layerIter.hasNext()) {
                // No tasks left in any layer, inform all listeners of completion
                for (Consumer<Boolean> listener : updateListeners) {
                    listener.accept(true);
                }
                updateListeners.clear();
                layers = null;
                if (debug) {
                    telemetry.log().add("RobotController finished executing layers");
                }
                return true;
            }
        }
        layerIter.previous(); // Throw away current layer
        Task task;
        while (true) {
            task = layer.update();
            if (!layerIter.hasPrevious()) {
                break; // Break before null check; drive layers may return null
            }
            if (task == null) {
                throw new NullPointerException("Layer '" + layer.getClass().getName()
                    + "' returned null from update.");
            }
            layer = layerIter.previous();
            layer.acceptTask(task);
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
