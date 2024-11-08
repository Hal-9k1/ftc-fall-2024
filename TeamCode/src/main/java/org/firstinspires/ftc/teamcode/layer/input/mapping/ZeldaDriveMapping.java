package org.firstinspires.ftc.teamcode.layer.input.mapping;

import org.firstinspires.ftc.teamcode.layer.FunctionLayer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.GamepadInputTask;
import org.firstinspires.ftc.teamcode.task.Task;
import org.firstinspires.ftc.teamcode.task.TankDriveTask;
import org.firstinspires.ftc.teamcode.task.UnsupportedTaskException;

/**
 * Mapping for gamepad input that uses the x and y components of the left
 * joystick for movement and turning, leaving the other joystick free.
 */
public class ZeldaDriveMapping extends FunctionLayer {
    @Override
    public void setup(LayerSetupInfo setupInfo) { }

    @Override
    public Task map(Task task) {
        if (task instanceof GamepadInputTask) {
            GamepadInputTask castedTask = (GamepadInputTask)task;
            double axial = castedTask.gamepad0.joysticks.left.y;
            double yaw = castedTask.gamepad0.joysticks.left.x;
            return new TankDriveTask(
                axial + yaw,
                axial - yaw
            );
        } else {
            throw new UnsupportedTaskException(this, task);
        }
    }
}
