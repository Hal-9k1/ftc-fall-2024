package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerHangTask;  // Either change this for TowerHangTeleopTask or change TowerHangTask to work with teleop.
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Button mappings for hanging the robot using tower.
 */
public final class TowerHangMapping extends AbstractFunctionLayer {
    /**
     * Constructor for TowerHangMapping.
     */
    public TowerHangMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask) task;
            boolean hang = castedTask.gamepad0.buttons.y;
            boolean unhang = castedTask.gamepad0.buttons.a;
            // Below bit is wrong so it will throw an exception if compiled. Need to fix later.
            return new TowerHangTask((hang ? 1 : 0) - (unhang ? 1 : 0));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
