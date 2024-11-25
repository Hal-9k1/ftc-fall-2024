package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

public class MultiplexLayer implements Layer {
    private final List<Layer> layers;

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