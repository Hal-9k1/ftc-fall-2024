package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Acts on behalf on multiple component layers to handle multiple unrelated kinds of tasks from the
 * layers above.
 * Needed because the RobotController reads layers as a stack, not a tree.
 */
public final class MultiplexLayer implements Layer {
    /**
     * The list of component layers.
     */
    private final List<Layer> layers;

    /**
     * Constructs a MultiplexLayer.
     *
     * @param layers - the layers this MultiplexLayer will contain.
     */
    public MultiplexLayer(List<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) {
        for (Layer layer : layers) {
            layer.setup(setupInfo);
        }
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        // Concatenates results of component layer update methods into a single stream, then creates
        // an iterator from the stream
        return layers.stream().flatMap(layer ->
            StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    layer.update(completed),
                    0
                ),
                false
            )
        ).iterator();
    }

    @Override
    public boolean isTaskDone() {
        return layers.stream().anyMatch(Layer::isTaskDone);
    }

    @Override
    public void acceptTask(Task task) {
        boolean anyAccepted = layers.stream().map((layer) -> {
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
}
