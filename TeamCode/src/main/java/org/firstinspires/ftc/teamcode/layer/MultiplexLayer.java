package org.firstinspires.ftc.teamcode.layer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Acts on behalf on multiple component layers to handle multiple unrelated kinds of tasks from the
 * layers above.
 * Needed because the RobotController reads layers as a stack, not a tree.
 */
public class MultiplexLayer implements Layer {
    /**
     * The list of component layers.
     */
    private final List<Layer> layers;

    /**
     * The telemetry used to report debugging info.
     */
    private Telemetry telemetry;

    /**
     * Constructs a MultiplexLayer.
     *
     * @param layers - the layers this MultiplexLayer will contain.
     */
    public MultiplexLayer(List<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public final void setup(LayerSetupInfo setupInfo) {
        for (Layer layer : layers) {
            layer.setup(setupInfo);
        }
        telemetry = setupInfo.getTelemetry();
    }

    @Override
    public final Iterator<Task> update(Iterable<Task> completed) {
        // Concatenates results of component layer update methods into a single stream, then creates
        // an iterator from the stream
        return layers.stream().flatMap(layer -> {
            // TODO: revert this after debugging; stuffing the tasks into an ArrayList defeats the
            // purpose of returning an iterator
            if (layer.isTaskDone()) {
                return Stream.of();
            }
            Iterator<Task> tasks = layer.update(completed);
            if (tasks == null) {
                throw new NullPointerException(
                    String.format(
                        "Tasks from layer '%s' is null.",
                        layer.getClass().getName()
                    )
                );
            }
            List<Task> taskList = new ArrayList<>();
            tasks.forEachRemaining(taskList::add);
            if (taskList.contains(null)) {
                throw new NullPointerException(
                    String.format(
                        "Tasks from layer '%s' contains null.",
                        layer.getClass().getName()
                    )
                );
            }

            return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    taskList.iterator(), // layer.update(completed),
                    0
                ),
                false
            );
        }).iterator();
    }

    @Override
    public final boolean isTaskDone() {
        telemetry.addLine(getClass().getName());
        for (Layer layer : layers) {
            if (layer.isTaskDone()) {
                telemetry.addLine("    " + layer.getClass().getName());
            }
        }
        Stream<Layer> stream = layers.stream();
        return isBlocking() ? stream.allMatch(Layer::isTaskDone) : stream.anyMatch(Layer::isTaskDone);
    }

    @Override
    public final void acceptTask(Task task) {
        boolean anyAccepted = layers.stream().map((layer) -> {
            if (!layer.isTaskDone() && isBlocking()) {
                throw new IllegalStateException("Blocking MultiplexLayer should not be giving new"
                    + " task to " + layer.getClass().getName());
            }
            try {
                layer.acceptTask(task);
            } catch (UnsupportedTaskException e) {
                return false;
            }
            return true;
        }).reduce(false, (a, b) -> a || b); // Prevent short circuiting
        if (!anyAccepted) {
            // Should list component layers, not say MultiplexLayer
            throw new UnsupportedTaskException(this, task);
        }
    }

    /**
     * Returns whether the MultiplexLayer should only report that it is done when all its component
     * layers do.
     * If false, it will report true for isTaskDone if any component layer does.
     * @return whether isTaskDone should wait for all component layers to report true. If false, it
     * will wait for any layer to return true.
     */
    protected boolean isBlocking() {
        return false;
    }
}
