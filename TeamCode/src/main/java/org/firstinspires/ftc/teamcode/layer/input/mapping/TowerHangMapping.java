package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerHangTask;
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
            // Not sure if this will be automatically set to false if another button is pressed. May need to check.
            boolean hang = castedTask.gamepad0.buttons.y;
            boolean unhang = castedTask.gamepad0.buttons.a;

            return new TowerHangTask(hang, unhang);
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
