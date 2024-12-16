package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.AbstractFunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TowerTeleopTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

public final class DpadTowerMapping extends AbstractFunctionLayer {
    public DpadTowerMapping() { }

    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            return new TowerTeleopTask((castedTask.gamepad0.dpad.up ? 1 : 0)
                - (castedTask.gamepad0.dpad.down ? 1 : 0));
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
