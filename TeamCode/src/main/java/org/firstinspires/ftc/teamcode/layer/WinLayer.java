package org.firstinspires.ftc.teamcode.layer;

import java.util.Collections;
import java.util.Iterator;

import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;
import org.firstinspires.ftc.teamcode.task.WinTask;

/**
 * Layer to tell the robot to win.
 * Though seemingly frivolous, required as the top layer on autonomous layer
 * stacks to tell the strategy layers to emit something.
 */
public class WinLayer implements Layer {
    private boolean emittedWin;

    public WinLayer() {
        emittedWin = false;
    }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public boolean isTaskDone() {
        return emittedWin;
    }

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
        emittedWin = true;
        return Collections.singleton((Task)(new WinTask())).iterator();
    }

    @Override
    public void acceptTask(Task task) {
        throw new UnsupportedTaskException(this, task);
    }
}
