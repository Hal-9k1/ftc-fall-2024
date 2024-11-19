package org.firstinspires.ftc.teamcode.layer;

import java.util.List;

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
    public Iterable<Task> update(Iterable<Task> completed) {
        // ??? How do we know which layer to ask for a new subtask from
        return null;
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
