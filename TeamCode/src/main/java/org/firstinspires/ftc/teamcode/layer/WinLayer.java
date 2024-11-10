package org.firstinspires.ftc.teamcode.layer;

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
    public Task update() {
        emittedWin = true;
        return new WinTask();
    }

    @Override
    public void acceptTask(Task task) {
        throw new UnsupportedTaskException(this, task);
    }
}