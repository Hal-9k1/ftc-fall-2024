package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

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
     * Constructs a MultiplexLayer.
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
        return layers.stream().flatMap(layer -> {
            Iterator<Task> tasks = layer.update(completed);
            if (tasks == null) {
                throw new NullPointerException("tasks from layer " + layer.getClass().getName() + " is null");
            }
            List<Task> taskList = new ArrayList<>();
            tasks.forEachRemaining(taskList::add);
            if (taskList.contains(null)) {
                throw new NullPointerException("tasks from layer " + layer.getClass().getName() + " contains null");
            }

            return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    taskList.iterator(),//layer.update(completed),
                    0
                ),
                false
            );
        }).iterator();
    }

    @Override
    public boolean isTaskDone() {
        return layers.stream().anyMatch(Layer::isTaskDone);
    }

    @Override
    public void acceptTask(Task task) {
        boolean anyAccepted = layers.stream().anyMatch((layer) -> {
            try {
                layer.acceptTask(task);
            } catch (UnsupportedTaskException e) {
                return false;
            }
            return true;
        });
        if (!anyAccepted) {
            // Should list component layers, not say MultiplexLayer
            throw new UnsupportedTaskException(this, task);
        }
    }
}
