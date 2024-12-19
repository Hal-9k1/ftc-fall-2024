package org.firstinspires.ftc.teamcode.layer;

import java.util.Iterator;
import java.util.List;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

public final class TopLayerSequence implements Layer {
    private Iterator<Layer> layerIter;
    private Layer layer;

    public TopLayerSequence(List<Layer> layers) {
        this.layerIter = layers.iterator();
        layer = layerIter.next();
    }

    @Override
    public boolean isTaskDone() {
        return layer.isTaskDone() && !layerIter.hasNext();
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        Iterator<Task> subtasks = layer.update(completed);
        if (layer.isTaskDone() && layerIter.hasNext()) {
            layer = layerIter.next();
        }
        return subtasks;
    }

    @Override
    public void acceptTask(Task task) {
        throw new UnsupportedTaskException(this, task);
    }
}
